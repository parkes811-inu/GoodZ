package com.springproject.goodz.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springproject.goodz.product.dto.Brand;
import com.springproject.goodz.product.service.BrandService;

import groovy.util.logging.Slf4j;

/*
 * [GET]    /admin              관리자 메인 화면
 * [GET]    /admin/brands       브랜드 리스트 화면
 * [GET]    /admin/add_brand    브랜드 등록 화면
 * [POST]   /admin/brands       브랜드 등록 처리
 */

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BrandService brandService;
    
    /**
     * 관리자 메인 화면
     * @return
     */
    @GetMapping("")
    public String index() {
        return "/admin/index";
    }

    /**
     * 브랜드 목록 화면
     * @return
     * @throws Exception 
     */
    @GetMapping("/brands")
    public String brand_list(Model model) throws Exception {

        List<Brand> brandList = brandService.list();

        model.addAttribute("brandList", brandList);

        return "/admin/brand_list";
    }

    /**
     * 브랜드 등록화면
     * @return
     */
    @GetMapping("/add_brand")
    public String moveToAddBrand() {
        return "/admin/add_brand";
    }


    // @PostMapping("/add_brand")
    // public String addBrand(Brand brand) {
        
    //     return entity;
    // }
    

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

    

    
}
