package com.bandit.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Bandit
 * @createTime 2022/6/28 16:01
 */
@Data
public class Message implements Serializable {

    // TODO 后期优化属性


    /**
     * 消息的id
     */
    private Long id;

    /**
     * 消息类型 1:建立连接； 2:发消息
     */
    private Integer type;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息发送者
     */
    private String sender;

    /**
     * 消息接收者
     */
    private String receiver;
}
