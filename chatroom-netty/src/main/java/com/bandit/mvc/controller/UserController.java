package com.bandit.mvc.controller;

import com.bandit.component.JWTComponent;
import com.bandit.entity.User;
import com.bandit.exception.XException;
import com.bandit.mvc.vo.ResultVO;
import com.bandit.mvc.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bandit
 * @createTime 2022/6/29 14:25
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final JWTComponent jwtComponent;

    public UserController(UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder, JWTComponent jwtComponent) {
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.jwtComponent = jwtComponent;
    }

    @PostMapping("/login")
    public ResultVO login(@RequestBody User user, HttpServletResponse response) {
        User u = userServiceImpl.getUserByAccount(user.getAccount());
        if (u == null || !passwordEncoder.matches(user.getPassword(), u.getPassword())) {
            throw new XException(401, "用户名密码错误！");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("uid", user.getId());
        response.addHeader("token", jwtComponent.encode(params));
        return ResultVO.success(params);
    }

}
