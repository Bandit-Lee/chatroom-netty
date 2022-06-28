package com.bandit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Bandit
 * @createTime 2022/6/28 17:43
 */
@Controller
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/chatroom")
    public String chatroom() {
        return "chatroom";
    }

}
