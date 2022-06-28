package com.bandit.handler;

import com.alibaba.fastjson.JSON;
import com.bandit.entity.Message;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
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

    /**
     * 用户名到Channel上下文的映射
     */
    private static final Map<String,ChannelHandlerContext> USER2CHANNEL_CTX = new ConcurrentHashMap<>(16);

    /**
     * channel ID 到 用户名的 映射
     */
    private static final Map<ChannelId, String> CHANNEL_ID2USER = new ConcurrentHashMap<>(16);

    /**
     * 已经连接到服务器的所有Channel上下文
     */
    private static final List<ChannelHandlerContext> CONNECTED_CHANNEL_CTX = new CopyOnWriteArrayList<>();


    /**
     * 注册一个Channel Handler Context
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("A Channel Registering.");
        CONNECTED_CHANNEL_CTX.add(ctx);
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
     * 处理下线
     * @param ctx
     */
    private void handleLogout(ChannelHandlerContext ctx) {
        ChannelId channelId = ctx.channel().id();
        String userName = CHANNEL_ID2USER.get(channelId);
        for (String name : USER2CHANNEL_CTX.keySet()) {
            if (name.equals(userName)) {
                continue;
            }
            Message message = new Message();
            message.setSender("[系统通知]");
            message.setType(2);
            message.setContent(userName + "下线了");
            ChannelHandlerContext context = USER2CHANNEL_CTX.get(name);
            context.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
        }
        //移除这个用户
        USER2CHANNEL_CTX.remove(userName);
        CONNECTED_CHANNEL_CTX.remove(ctx);
        CHANNEL_ID2USER.remove(channelId);

    }

    /**
     * 处理channelRead0的消息Message
     * @param ctx
     * @param message
     */
    private void handleMessage(ChannelHandlerContext ctx, Message message) {
        Integer type = message.getType();
        String sender = message.getSender();
        String receiver = message.getReceiver();
        //TODO Message Type枚举后期
        if (type == 1) {
            //建立连接
            message.setContent(sender+"上线了...");
            USER2CHANNEL_CTX.put(sender, ctx);
            CHANNEL_ID2USER.put(ctx.channel().id(), sender);
            // 遍历所有channel, 发送上线通知
            // TODO 上线后续指定群组
            for (String userName : USER2CHANNEL_CTX.keySet()) {
                if (userName.equals(sender)) {
                    continue;
                }
                ChannelHandlerContext context = USER2CHANNEL_CTX.get(userName);
                context.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
            }
        } else if (type == 2) {
            // 发消息
            // 没指定接收者就群发
            // TODO 发消息 后续修改逻辑
            if (StringUtil.isNullOrEmpty(receiver)) {
                for (String userName : USER2CHANNEL_CTX.keySet()) {
                    if (userName.equals(sender)) {
                        continue;
                    }
                    ChannelHandlerContext context = USER2CHANNEL_CTX.get(userName);
                    context.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                }
            } else {
                // 指定了单聊
                if (!USER2CHANNEL_CTX.containsKey(receiver)) {
                    // 不存在 ===> 下线了
                    // TODO 下线后任然保存消息
                    Message msgToSender = new Message();
                    msgToSender.setSender("[系统通知]");
                    msgToSender.setReceiver(sender);
                    msgToSender.setType(2);
                    msgToSender.setContent("用户已下线, 你的消息未能送达");
                    ctx.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(msgToSender)));
                }
            }
        }

    }




}
