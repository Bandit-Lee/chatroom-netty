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
 * @createTime 2022/6/29 23:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @CreatedBy
    private Long ruId;
    private Long roomId;
    private Long userId;

}
