package com.bandit.service;

import com.bandit.entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Bandit
 * @createTime 2022/6/29 16:58
 */
@Service
public class RoomServiceImpl {

    private static final Map<Long, CopyOnWriteArrayList<User>> roomMap = new ConcurrentHashMap<>(16);

    RoomServiceImpl() {
        CopyOnWriteArrayList<User> userList = new CopyOnWriteArrayList<>();
        userList.add(new User(1L, "bandit", "1001","123456"));
        userList.add(new User(2L, "aaa", "1002","123456"));
        userList.add(new User(3L, "bbb","1003", "123456"));
        CopyOnWriteArrayList<User> userList2 = new CopyOnWriteArrayList<>();
        userList2.add(new User(4L, "ccc", "1004","123456"));
        userList2.add(new User(5L, "ddd","1005", "123456"));
        userList2.add(new User(6L, "eee","1006", "123456"));
        userList2.add(new User(7L, "fff","1007", "123456"));
        roomMap.put(1L,userList);
        roomMap.put(2L,userList2);
    }

    /**
     * 获得 房间号 ==》 用户的映射. 后期用连接表
     * @return
     */
    public Map<Long, CopyOnWriteArrayList<User>> getRoomMap() {
        return roomMap;
    }

}
