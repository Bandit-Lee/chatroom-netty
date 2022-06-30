package com.bandit.mvc.service.impl;

import com.bandit.entity.User;
import com.bandit.mvc.mapper.UserMapper;
import com.bandit.mvc.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Bandit
 * @createTime 2022/6/29 14:14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;

    UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUserList() {
        return baseMapper.selectList(null);
    }
    public User getUserById(Long id) {
        return baseMapper.selectById(id);
    }

    public User getUserByAccount(String account) {
        return baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getAccount, account));
    }

    @Transactional
    public int saveAndEncode(User user) {
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        return baseMapper.insert(user);
    }


}
