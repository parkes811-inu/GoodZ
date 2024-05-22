package com.springproject.goodz.sns.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sns")
public class exampleController {

    @GetMapping("/{page}")
    public String controller(@PathVariable("page") String page) {
            return "/sns/" + page;
    }
}