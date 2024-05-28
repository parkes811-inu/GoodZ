package com.springproject.goodz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;



@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {
    
    @GetMapping("/buy")
    public String buy() {
        return "/pay/buy";
    }

    @GetMapping("/success")
    public String success() {
        return "/pay/success";
    }
    
    @GetMapping("/fail")
    public String fail() {
        return "/pay/fail";
    }

    @GetMapping("/complete")
    public String complete() {
        return "/pay/complete";
    }

    @GetMapping("/sell")
    public String sell() {
        return "/pay/sell";
    }
}
