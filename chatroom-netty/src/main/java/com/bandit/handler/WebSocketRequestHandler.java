package com.bandit.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;


/**
 * 处理WebSocket协议升级
 * @author Bandit
 * @createTime 2022/6/28 16:30
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class WebSocketRequestHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker handshaker;

    @Value("${chatroom.server.path}")
    private String webSocketPath;

    /**
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    /**
     * 有请求来的时候，将Http请求转换为WebSocket，如果是WebSocket就开始建立连接
     * @param ctx           the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *                      belongs to
     * @param msg           the message to handle
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            //需要处理Http到WebSocket的升级
            log.info("Http upgrade");
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleHttpWebSocket(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * 处理WebSocketFrame
     * @param ctx
     * @param frame
     */
    private void handleHttpWebSocket(ChannelHandlerContext ctx, WebSocketFrame frame) {
        //判断是否是链路关闭请求
        if (frame instanceof CloseWebSocketFrame) {
            log.info("CloseWebSocketFrame request received. Start close the WebSocket.");
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        //判断是否是PING
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        //不支持二进制消息
        //TODO 后续加上 消息类型 校验
        if (!(frame instanceof TextWebSocketFrame)) {
            throw new UnsupportedOperationException(String.format("%s frame types not supported", frame.getClass().getName()));
        }
        //解决 refCnt: 0, decrement: 1
        ReferenceCountUtil.retain(frame);
        //通知后续的消息处理器
        ctx.fireChannelRead(frame);
    }

    /**
     * 处理Http请求
     * @param ctx
     * @param req
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        //如果Http解码失败返回Http异常
        if (!req.decoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        //构造webSocket握手响应
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8080" + webSocketPath, null, false);
        handshaker = wsFactory.newHandshaker(req);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            handshaker.handshake(ctx.channel(), req);
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res) {
        //返回响应给客户端
        if (res.status().code() != 200) {
            ByteBuf byteBuf = Unpooled.copiedBuffer(res.status().toString(), StandardCharsets.UTF_8);
            res.content().writeBytes(byteBuf);
            byteBuf.release();
            HttpHeaders.setContentLength(res, res.content().readableBytes());
        }

        //如果是非Keep-Alive，关闭连接
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(res);
        if (!HttpHeaders.isKeepAlive(req) || res.status().code() != 200) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn(cause.getMessage());
        ctx.close();
    }
}
