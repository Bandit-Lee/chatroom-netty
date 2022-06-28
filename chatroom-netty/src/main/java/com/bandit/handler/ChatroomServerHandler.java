package com.bandit.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Bandit
 * @createTime 2022/6/28 17:02
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class ChatroomServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
        log.info("Receive Client: " + frame.text());
        Channel channel = ctx.channel();
        channel.write(new TextWebSocketFrame(frame.text() + "marked"));
    }
}
