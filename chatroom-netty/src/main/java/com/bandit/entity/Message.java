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
    private static final long serialVersionUID = 1L;

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
    private Long senderId;

    /**
     * 消息接收者
     */
    private Long receiverId;

    /**
     * 房间号
     */
    private Long roomId;

    /**
     * 时间戳
     */
    private Long timeStamp;

    public Message() {
        timeStamp = System.currentTimeMillis();
    }
}
