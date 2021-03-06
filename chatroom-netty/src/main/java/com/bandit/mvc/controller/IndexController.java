package com.bandit.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Bandit
 * @createTime 2022/6/28 17:43
 */
@Controller
public class IndexController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/chatroom")
    public String chatroom() {
        return "chat/chatroom";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
