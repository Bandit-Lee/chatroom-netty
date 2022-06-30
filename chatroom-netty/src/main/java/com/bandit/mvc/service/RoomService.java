package com.bandit.mvc.service;

import com.bandit.entity.Room;
import com.bandit.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @author Bandit
 * @createTime 2022/6/29 23:37
 */
public interface RoomService extends IService<Room> {

    Map<Room, List<User>> getRoomMap();

    List<Room> getRoomByUserId(Long uid);
}
