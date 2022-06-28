package com.bandit.server;

import com.bandit.handler.ChatroomServerHandler;
import com.bandit.handler.WebSocketRequestHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Bandit
 * @createTime 2022/6/28 16:13
 */
@Component
public class ChatroomServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private WebSocketRequestHandler webSocketRequestHandler;

    @Autowired
    private ChatroomServerHandler chatroomServerHandler;

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //HttpServerCodec：Http的编解码器
        pipeline.addLast("http-codec", new HttpServerCodec());
        //ChunkedWriteHandler：用于支持大数据流
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
        //HttpObjectAggregator：将Http消息多个部分组合成一条完整的Http消息
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        //TODO 添加WebSocket支持
        pipeline.addLast("ws-request-handler", webSocketRequestHandler);
        //添加自定义WebSocket消息处理器
        pipeline.addLast("ws-message-handler", chatroomServerHandler);
    }
}
