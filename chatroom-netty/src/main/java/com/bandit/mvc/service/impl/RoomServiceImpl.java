package com.bandit.mvc.service.impl;

import com.bandit.entity.Room;
import com.bandit.entity.RoomUser;
import com.bandit.entity.User;
import com.bandit.mvc.dto.RoomUserDto;
import com.bandit.mvc.mapper.RoomMapper;
import com.bandit.mvc.service.RoomService;
import com.bandit.mvc.service.RoomUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Bandit
 * @createTime 2022/6/29 16:58
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    @Autowired
    RoomUserServiceImpl roomUserService;

    @Autowired
    UserServiceImpl userService;

    RoomServiceImpl() {
    }

    /**
     * 获得 房间 ==》 用户的映射. 后期用连接表
     * @return
     */
    public Map<Room, List<User>> getRoomMap() {
        List<RoomUser> roomUserList = roomUserService.list();
        List<Room> roomList = baseMapper.selectList(null);
        return roomList.stream().map(room -> {
            RoomUserDto dto = new RoomUserDto();
            Long id = room.getId();
            List<User> userList = roomUserList
                    .stream()
                    .filter(roomUser -> Objects.equals(roomUser.getRoomId(), id))
                    .map(roomUser -> userService.getUserById(roomUser.getUserId()))
                    .collect(Collectors.toList());
            dto.setUserList(userList);
            return dto;
        }).collect(Collectors.toMap(RoomUserDto::getRoom, RoomUserDto::getUserList));
    }

    @Override
    public List<Room> getRoomByUserId(Long uid) {
        //查出所有关联关系
        List<RoomUser> roomUserList = roomUserService
                .getBaseMapper()
                .selectList(new LambdaQueryWrapper<RoomUser>().eq(RoomUser::getUserId, uid));
        if (roomUserList == null || roomUserList.size() == 0) {
            return null;
        }
        List<Long> roomIds = roomUserList.stream().map(RoomUser::getRoomId).collect(Collectors.toList());
        return baseMapper.selectBatchIds(roomIds);
    }

}
