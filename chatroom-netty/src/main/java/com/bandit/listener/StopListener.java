package com.bandit.listener;

import com.bandit.server.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;

/**
 * @author Bandit
 * @createTime 2022/6/30 11:42
 */
public class StopListener implements ApplicationListener<ContextStoppedEvent> {

    @Autowired
    NettyServer nettyServer;

    @Override
    public void onApplicationEvent(ContextStoppedEvent event) {
        nettyServer.shutdown();
    }
}
