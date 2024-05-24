package com.springproject.goodz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;



@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    
    @GetMapping("/index")
    public String index() {
        return "/user/index";
    }
    
}
