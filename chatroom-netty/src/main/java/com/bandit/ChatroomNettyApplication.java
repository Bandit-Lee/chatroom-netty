package com.bandit;

import com.bandit.server.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatroomNettyApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ChatroomNettyApplication.class, args);
    }

    @Autowired
    NettyServer nettyServer;

    @Override
    public void run(String... args) throws Exception {
        nettyServer.start();
    }
}
