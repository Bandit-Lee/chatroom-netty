package com.bandit.mvc.service;

import com.bandit.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Bandit
 * @createTime 2022/6/29 23:26
 */
public interface UserService extends IService<User> {

    List<User> getUserList();

    User getUserById(Long id);

    User getUserByAccount(String account);

    int saveAndEncode(User user);
}
