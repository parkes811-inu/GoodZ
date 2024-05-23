package com.springproject.goodz.style.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@Controller
@RequestMapping("/style")
public class StyleController {
    
    @GetMapping("/list")
    public String list() {
        return "/style/list";
    }
    
}
