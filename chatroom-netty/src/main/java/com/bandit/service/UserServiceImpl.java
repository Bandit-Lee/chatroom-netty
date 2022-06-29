package com.bandit.service;

import com.bandit.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Bandit
 * @createTime 2022/6/29 14:14
 */
@Service
public class UserServiceImpl {

    //TODO 后期与数据库交互
    private static final List<User> userList = new CopyOnWriteArrayList<>();

    UserServiceImpl() {
        userList.add(new User(1L, "bandit", "1001","123456"));
        userList.add(new User(2L, "aaa", "1002","123456"));
        userList.add(new User(3L, "bbb","1003", "123456"));
        userList.add(new User(4L, "ccc", "1004","123456"));
        userList.add(new User(5L, "ddd","1005", "123456"));
        userList.add(new User(6L, "eee","1006", "123456"));
        userList.add(new User(7L, "fff","1007", "123456"));
    }

    public List<User> getUserList() {
        return userList;
    }

    /**
     * 通过id查User
     * @param id 用户id
     * @return
     */
    public User getUserById(Long id) {
        return userList.stream()
                .filter(user -> Objects.equals(user.getId(), id))
                .findFirst()
                .orElse(null);
    }


    public User getUserByAccount(String account) {
        return userList.stream()
                .filter(user -> user.getAccount().equals(account))
                .findFirst()
                .orElse(null);
    }

}
