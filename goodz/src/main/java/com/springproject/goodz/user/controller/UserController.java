package com.springproject.goodz.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/user")
public class UserController {
    
    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }
    
    @GetMapping("/signup")
    public String signup() {
        return "/user/signup";
    }

    @GetMapping("/signup2")
    public String signup2() {
        return "/user/signup2";
    }

    @GetMapping("/findID")
    public String findID() {
        return "/user/findID";
    }

    @GetMapping("/findPW")
    public String findPW() {
        return "/user/findPW";
    }
}
