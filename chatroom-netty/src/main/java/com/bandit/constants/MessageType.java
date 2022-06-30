package com.bandit.constants;

/**
 * @author Bandit
 * @createTime 2022/6/29 16:21
 */
public enum MessageType {

    LINK(1, "上线消息"),
    GROUP_SEND(2, "群聊"),
    SINGLE_SEND(3, "私聊");

    private int code;
    private String info;

    MessageType(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
