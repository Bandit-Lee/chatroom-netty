package com.bandit.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @author Bandit
 * @createTime 2022/6/29 14:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Id
    @CreatedBy
    private Long id;
    /**
     * 姓名
     */
    private String userName;
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String password;

}
