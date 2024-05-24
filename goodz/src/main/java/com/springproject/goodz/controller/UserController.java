package com.springproject.goodz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;



@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    
    @GetMapping("")
    public String index() {
        return "/user/index";
    }
    
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
