package com.bandit.vo;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultVO {
    private int code;
    private String message;
    private Map<String, Object> data;

    public static ResultVO success() {
        return ResultVO.builder().code(200).build();
    }

    public static ResultVO success(Map<String, Object> data) {
        return ResultVO.builder().code(200).data(data).build();
    }

    public static ResultVO error(int code, String msg) {
        return ResultVO.builder().code(code).message(msg).build();
    }
}
