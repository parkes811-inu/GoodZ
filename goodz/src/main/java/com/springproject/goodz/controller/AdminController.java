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

    @GetMapping("/purchase/detail")
    public String purchase_detail() {
        return "/admin/purchase_detail";
    }

    @GetMapping("/pay_history")
    public String pay_history() {
        return "/admin/pay_history";
    }

    @GetMapping("/pay_history/detail")
    public String pay_history_detail() {
        return "/admin/pay_history_detail";
    }

    @GetMapping("/add_product")
    public String add_product() {
        return "/admin/add_product";
    }

    @GetMapping("/product/detail")
    public String product_detail() {
        return "/admin/product_detail";
    }

    @GetMapping("/add_brand")
    public String add_brand() {
        return "/admin/add_brand";
    }
}
