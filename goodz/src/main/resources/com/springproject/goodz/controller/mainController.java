package com.springproject.goodz.controller;

import javax.websocket.server.PathParam;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class mainController {
    
    public String mainController(@PathParam("{page}") String page) {
        return "/" + page;
    }
}
