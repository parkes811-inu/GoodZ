package com.springproject.goodz.controller;

import java.net.URLEncoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.service.ProductService;

import lombok.extern.slf4j.Slf4j;





@Slf4j
@Controller
@RequestMapping("")
public class MainController {

    @Autowired
    private ProductService productService;
 
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
    
    @GetMapping("")
    public String newArrivals(Model model) throws Exception {
        List<Product> newArrivalsList = productService.newArrivals();

        
        for (Product product : newArrivalsList) {
            String firstImageUrl = product.getFirstImageUrl();
            if (firstImageUrl != null) {
            
                product.setImageUrl(URLEncoder.encode(firstImageUrl, "UTF-8")); // URL 인코딩 적용
            }
        }

        model.addAttribute("newArrivalsList", newArrivalsList);
        return "/index";
    }
}