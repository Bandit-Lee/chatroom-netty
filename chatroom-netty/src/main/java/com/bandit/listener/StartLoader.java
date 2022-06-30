package com.bandit.listener;

import com.bandit.server.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author Bandit
 * @createTime 2022/6/29 15:45
 */
@Component
public class StartLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    NettyServer nettyServer;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            nettyServer.start();
        }
    }
}
