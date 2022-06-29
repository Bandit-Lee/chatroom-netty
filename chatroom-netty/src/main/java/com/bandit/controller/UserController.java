package com.bandit.controller;

import com.bandit.entity.User;
import com.bandit.service.UserServiceImpl;
import com.bandit.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bandit
 * @createTime 2022/6/29 14:25
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResultVO login(@RequestBody User user) {
        User userByAccount = userService.getUserByAccount(user.getAccount());
        if (userByAccount != null) {
            log.info("user:{}",userByAccount);
            Map<String, Object> params = new HashMap<>();
            params.put("user", userByAccount);
            return ResultVO.success(params);
        }
        return ResultVO.error(401, "用户名密码错误");
    }

}
