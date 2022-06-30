package com.bandit.handler;

import com.alibaba.fastjson.JSON;
import com.bandit.entity.Message;
import com.bandit.constants.MessageType;
import com.bandit.entity.User;
import com.bandit.mvc.service.impl.UserServiceImpl;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Bandit
 * @createTime 2022/6/28 17:02
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class ChatroomServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Autowired
    UserServiceImpl userServiceImpl;


    /**
     * 用户到 Channel上下文的映射
     */
    private static final Map<User,ChannelHandlerContext> USER2CHANNEL_CTX = new ConcurrentHashMap<>(16);

    /**
     * channel ID 到 用户id 的 映射
     */
    private static final Map<ChannelId, User> CHANNEL_ID2USER = new ConcurrentHashMap<>(16);

    /**
     * 已经连接到服务器的所有Channel上下文
     */
    private static final List<ChannelHandlerContext> CONNECTED_CHANNEL_CTX = new CopyOnWriteArrayList<>();

    /**
     * 上线时 加入房间的用户
     */
    private static Map<Long, CopyOnWriteArrayList<User>> roomStubMap = new ConcurrentHashMap<>(16);



    /**
     * 注册一个Channel Handler Context
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("A Channel Registering.");
        handleLogin(ctx);
        ctx.fireChannelActive();
    }


    /**
     * 下线
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("A Channel Logout");
        handleLogout(ctx);
        ctx.fireChannelInactive();
    }

    /**
     * 读到一条 WebSocketFrame
     * @param ctx           the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *                      belongs to
     * @param frame           the message to handle
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        log.info("Receive Client: " + frame.text());
        //TODO 这里可以交给一个线程写入数据库
        Message message = JSON.parseObject(frame.text(), Message.class);
        handleMessage(ctx, message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("Exception Found");
        handleLogout(ctx);
        ctx.fireExceptionCaught(cause);
    }

    /**
     * 处理上线
     * @param ctx
     */
    private void handleLogin(ChannelHandlerContext ctx) {
        log.info("Created a Channel.");
        CONNECTED_CHANNEL_CTX.add(ctx);
    }

    /**
     * 处理下线
     * @param ctx
     */
    private void handleLogout(ChannelHandlerContext ctx) {
        ChannelId channelId = ctx.channel().id();
        User user = CHANNEL_ID2USER.get(channelId);
        for (User u : USER2CHANNEL_CTX.keySet()) {
            if (Objects.equals(u.getId(), user.getId())) {
                continue;
            }
            Message message = new Message();
            message.setSenderId(0L);
            message.setType(2);
            message.setContent("[系统信息]:"+ user.getUserName() + "下线了");
            ChannelHandlerContext context = USER2CHANNEL_CTX.get(u);
            context.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
        }
        //移除这个用户
        USER2CHANNEL_CTX.remove(user);
        CONNECTED_CHANNEL_CTX.remove(ctx);
        CHANNEL_ID2USER.remove(channelId);
        for (CopyOnWriteArrayList<User> users : roomStubMap.values()) {
            users.remove(user);
        }

    }

    /**
     * 处理channelRead0的消息Message
     * @param ctx
     * @param message
     */
    private void handleMessage(ChannelHandlerContext ctx, Message message) {
        Integer type = message.getType();
        Long senderId = message.getSenderId();
        Long roomId = message.getRoomId();
        // receiverId 可能为空
        Long receiverId = message.getReceiverId();
        User sender = userServiceImpl.getUserById(senderId);
        //====================================建立连接请求=========================================
        if (type == MessageType.LINK.getCode()) {
            message.setContent("[系统信息]===>"+ roomId + "号房间: " + sender.getUserName()+"上线了...");
            // 上线时
            USER2CHANNEL_CTX.put(sender, ctx);
            CHANNEL_ID2USER.put(ctx.channel().id(), sender);
            if (roomStubMap.get(roomId) != null) {
                roomStubMap.get(roomId).add(sender);
            } else {
                roomStubMap.put(roomId, new CopyOnWriteArrayList<>(new User[]{sender}));
            }
            // 获取同一房间在线的用户，遍历这些用户
            CopyOnWriteArrayList<User> roomUsers = roomStubMap.get(roomId);
            for (User u : roomUsers) {
                if (Objects.equals(u.getId(), senderId)) {
                    continue;
                }
                ChannelHandlerContext context = USER2CHANNEL_CTX.get(u);
                context.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
            }

        //====================================群发=========================================
        } else if (type == MessageType.GROUP_SEND.getCode()) {
            // 没指定接收者就群发
            // TODO 发消息 后续修改逻辑
            if (receiverId == null) {
                // 获取同一房间在线的用户，遍历这些用户
                CopyOnWriteArrayList<User> roomUsers = roomStubMap.get(roomId);
                if (roomUsers != null) {
                    message.setContent(sender.getUserName()+"说："+message.getContent());
                    for (User u : roomUsers) {
                        if (Objects.equals(u.getId(), senderId)) {
                            continue;
                        }
                        ChannelHandlerContext context = USER2CHANNEL_CTX.get(u);
                        context.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                    }
                }
            }
        //====================================私聊=========================================
        } else if (type == MessageType.SINGLE_SEND.getCode()) {
            // 指定了单聊
            User receiver = userServiceImpl.getUserById(receiverId);
            if (!USER2CHANNEL_CTX.containsKey(receiver)) {
                // 不存在 ===> 下线了
                // TODO 下线后任然保存消息
                Message msgToSender = new Message();
                msgToSender.setSenderId(0L);
                msgToSender.setSenderId(senderId);
                msgToSender.setType(2);
                msgToSender.setContent("[系统信息]:用户已下线, 你的消息未能送达");
                ctx.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msgToSender)));
            }
        }

    }




}
