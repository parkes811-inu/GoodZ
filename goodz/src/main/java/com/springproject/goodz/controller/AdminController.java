package com.springproject.goodz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.util.logging.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {
    
    @GetMapping("")
    public String index() {
        return "/admin/index";
    }

    @GetMapping("/brand_list")
    public String brand_list() {
        return "/admin/brand_list";
    }

    @GetMapping("/product_list")
    public String product_list() {
        return "/admin/product_list";
    }

    @GetMapping("/purchase_state")
    public String purchase_state() {
        return "/admin/purchase_state";
    }

    @GetMapping("/pay_history")
    public String pay_history() {
        return "/admin/pay_history";
    }
}
