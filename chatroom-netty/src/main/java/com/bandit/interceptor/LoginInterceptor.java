package com.bandit.interceptor;

import com.bandit.component.JWTComponent;
import com.bandit.exception.XException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Bandit
 * @createTime 2022/6/30 12:03
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JWTComponent jwtComponent;
    public LoginInterceptor(JWTComponent jwtComponent) {
        this.jwtComponent = jwtComponent;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token == null) {
            throw new XException(401, "未登录");
        }
        Long uid = jwtComponent.decode(token).getClaim("uid").asLong();
        request.setAttribute("uid", uid);
        return true;
    }
}
