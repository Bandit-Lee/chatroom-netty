package com.bandit.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Bandit
 * @createTime 2022/6/28 16:07
 */
@Component
@Slf4j
public class NettyServer {

    /**
     * 服务器绑定的port
     */
    @Value("${chatroom.server.port}")
    private int port;

    private EventLoopGroup boosGroup;
    private EventLoopGroup workerGroup;

    @Autowired
    private ChatroomServerInitializer chatroomServerInitializer;


    public void start() {
        boosGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(chatroomServerInitializer);

        ChannelFuture channelFuture = null;
        try {
            channelFuture = serverBootstrap.bind(port).sync();

        } catch (InterruptedException e) {
            e.printStackTrace();

        }
        log.info("Chatroom Server Started.");
        channelFuture.channel().closeFuture();

    }

    public void shutdown() {
        boosGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }


}
