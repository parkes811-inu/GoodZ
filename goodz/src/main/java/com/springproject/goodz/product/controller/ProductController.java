package com.springproject.goodz.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.service.ProductService;

import lombok.extern.slf4j.Slf4j;




@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping("")
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

    @GetMapping("/top")
    public String top(Model model) throws Exception {

        List<Product> topList = productService.top();

        model.addAttribute("topList", topList);

        return "/product/top";
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
