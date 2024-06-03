package com.springproject.goodz.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.dto.ProductOption;
import com.springproject.goodz.product.service.ProductService;
import com.springproject.goodz.utils.dto.Files;
import com.springproject.goodz.utils.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController {

    @Value("${upload.path}") // application.properties 에 설정한 업로드 경로 가져옴
    private String uploadPath;

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @GetMapping("")
    public String index() {
        return "/product/index";
    }

    @GetMapping("/product/size_table")
    public String productSizeInfoPage() {
        return "/fragments/product/size_table";
    }

    @GetMapping("/detail/{pNo}")
    public String productDetailPage(@PathVariable("pNo") Integer pNo, Model model) throws Exception {
        Product product = productService.getProductBypNo(pNo);
        List<ProductOption> option =  productService.getProductOptionsByProductId(pNo);
        Files file = new Files();
        file.setParentNo(pNo);
        file.setParentTable(product.getCategory());
        List<Files> images = fileService.listByParent(file);
        model.addAttribute("product", product);
        model.addAttribute("option", option);
        model.addAttribute("images", images);

        return "/product/detail";
    }

    // 상의 카테고리
    @GetMapping("/top")
    public String top(Model model) throws Exception {
        List<Product> topList = productService.top();
        Files file = new Files();
        file.setParentTable("top");
        List<Files> images = fileService.listByParent(file);

        model.addAttribute("topList", topList);
        model.addAttribute("images", images);

        return "/product/top";
    }

    // 하의 카테고리
    @GetMapping("/pants")
    public String pants(Model model) throws Exception {
        List<Product> pantsList = productService.pants();
        Files file = new Files();
        file.setParentTable("pants");
        List<Files> images = fileService.listByParent(file);

        model.addAttribute("images", images);
        model.addAttribute("pantsList", pantsList);

        return "/product/pants";
    }

    // 신발 카테고리
    @GetMapping("/shoes")
    public String shoes(Model model) throws Exception {
        List<Product> shoesList = productService.shoes();
        Files file = new Files();
        file.setParentTable("shoes");
        List<Files> images = fileService.listByParent(file);

        model.addAttribute("images", images);
        model.addAttribute("shoesList", shoesList);

        return "/product/shoes";
    }

    // 악세사리 카테고리
    @GetMapping("/accessory")
    public String accessory(Model model) throws Exception {
        List<Product> accessoryList = productService.accessory();
        Files file = new Files();
        file.setParentTable("pants");
        List<Files> images = fileService.listByParent(file);
        
        model.addAttribute("images", images);
        model.addAttribute("accessoryList", accessoryList);

        return "/product/accessory";
    }
}
