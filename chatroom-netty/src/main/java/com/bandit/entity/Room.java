package com.bandit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * 聊天室
 * @author Bandit
 * @createTime 2022/6/29 16:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 聊天室主键
     */
    @Id
    @CreatedBy
    private Long id;

    /**
     * 聊天室介绍
     */
    private String name;
}
