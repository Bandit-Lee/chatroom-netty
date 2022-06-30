package com.bandit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bandit.mvc.mapper")
public class ChatroomNettyApplication{

    public static void main(String[] args) {
        SpringApplication.run(ChatroomNettyApplication.class, args);
    }
}
