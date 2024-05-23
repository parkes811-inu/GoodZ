package com.springproject.goodz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.util.logging.Slf4j;


@Slf4j
@Controller
@RequestMapping("/")
public class MainController {

 
    @GetMapping("/{page}")
    public String page(@PathVariable("page") String page) {
        return page;
    }


    // footer 하단 링크 
    @GetMapping("/footer/{id}")
    public String getFooterMapping(@PathVariable("id") int id, Model model) {
        String template;
        switch (id) {
            case 1:
                template = "common/privacy"; // templates/common/privacy.html
                break;
            case 2:
                template = "common/inspection"; // templates/common/inspection.html
                break;
            case 3:
                template = "common/store_info"; // templates/common/store_info.html
                break;
            case 4:
                template = "common/guideLine"; // templates/common/community_guidelines.html
                break;
            default:
                template = "/"; // default to home
        }
        return template;
    }
    
    @GetMapping("/product")
    public String productPage() {
        return "product/index";
    }

}