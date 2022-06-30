package com.bandit.mvc.repository;

import com.bandit.entity.Room;
import com.bandit.entity.RoomUser;
import com.bandit.entity.User;
import com.bandit.mvc.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Bandit
 * @createTime 2022/6/29 22:18
 */
@SpringBootTest
public class RepositoryTests {


    @Autowired
    UserServiceImpl userServiceImpl;


    @Test
    public void test() {
        List<User> userList = new ArrayList<>();
        userList.add(User.builder().userName("aaa").account("1002").password("123456").build());
        userList.add(User.builder().userName("bbb").account("1003").password("123456").build());
        userList.add(User.builder().userName("ccc").account("1004").password("123456").build());
        userList.add(User.builder().userName("ddd").account("1005").password("123456").build());
        userList.add(User.builder().userName("eee").account("1006").password("123456").build());
        userList.add(User.builder().userName("fff").account("1007").password("123456").build());

        userServiceImpl.saveBatch(userList);
    }

}
