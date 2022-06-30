package com.bandit.mvc.dto;

import com.bandit.entity.Room;
import com.bandit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Bandit
 * @createTime 2022/6/29 23:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomUserDto implements Serializable {

    private Long ruId;

    private Room room;

    private List<User> userList;

}
