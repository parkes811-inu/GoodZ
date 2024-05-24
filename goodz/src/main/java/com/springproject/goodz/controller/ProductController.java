package com.springproject.goodz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.util.logging.Slf4j;




@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController {
    
    @GetMapping("/")
    public String index() {
        return "/product/index";
    }

    @GetMapping("/product/size_table")
    public String productSizeInfoPage() {
        return "/fragments/product/size_table";
    }

    @GetMapping("/detail")
    public String productDetailPage() {
        return "/product/detail";
    }

    @GetMapping("/shirts")
    public String productShirtsPage() {
        return "/product/shirts";
    }

    @GetMapping("/pants")
    public String productPantsPage() {
        return "/product/pants";
    }

    @GetMapping("/shoes")
    public String productShoesPage() {
        return "/product/shoes";
    }

    @GetMapping("/accessory")
    public String productAccessoryPage() {
        return "/product/accessory";
    }
}
