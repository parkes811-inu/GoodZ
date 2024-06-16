package com.springproject.goodz.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springproject.goodz.product.dto.Pricehistory;
import com.springproject.goodz.product.service.ProductService;

@RestController
@RequestMapping("/api")
public class PriceController {

    @Autowired
    ProductService productService;

    // 상품 가격 변동 History 조회
    @GetMapping("/getPriceHistory")
    public List<Pricehistory> getPriceHistory(@RequestParam String period, @RequestParam int pNo) throws Exception {
        return productService.getPriceHistory(period, pNo);
    }
}
