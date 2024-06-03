package com.springproject.goodz.product.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.dto.ProductImage;
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
    public String index(Model model) throws Exception {

        List<Product> productList = productService.list();

        for (Product product : productList) {
            // 상품 옵션 설정
            List<ProductOption> options = productService.getProductOptionsByProductId(product.getPNo());
            product.setOptions(options);

            // 상품 이미지 설정
            Files file = new Files();
            file.setParentNo(product.getPNo());
            file.setParentTable(product.getCategory());
            List<Files> productImages = fileService.listByParent(file);
            
            // 첫 번째 이미지 URL 설정
            if (!productImages.isEmpty()) {
                product.setImageUrl(productImages.get(0).getFilePath());
            } else {
                product.setImageUrl("/files/img?imgUrl=no-image.png"); // 기본 이미지 경로 설정
            }
        }

        model.addAttribute("productList", productList);
        return "/product/index";
    }

    @GetMapping("/product/size_table")
    public String productSizeInfoPage() {
        return "/fragments/product/size_table";
    }

    @GetMapping("/detail/{pNo}")
    public String productDetailPage(@PathVariable("pNo") Integer pNo, Model model) throws Exception {
        Product product = productService.getProductBypNo(pNo);
        List<ProductOption> options = productService.getProductOptionsByProductId(pNo);
        product.setOptions(options);

        Files file = new Files();
        file.setParentNo(pNo);
        file.setParentTable(product.getCategory());
        List<Files> images = fileService.listByParent(file);

        // 최저 가격 계산
        int minPrice = options.stream()
                            .mapToInt(ProductOption::getOptionPrice)
                            .min()
                            .orElse(0);

        model.addAttribute("product", product);
        model.addAttribute("options", options);
        model.addAttribute("images", images);
        model.addAttribute("minPrice", minPrice);

        // 사이즈별 가격 정보를 JSON 형태로 변환
        String pricesJson = new ObjectMapper().writeValueAsString(
            options.stream().collect(Collectors.toMap(ProductOption::getSize, ProductOption::getOptionPrice))
        );
        model.addAttribute("pricesJson", pricesJson);

        return "/product/detail";
    }



    // 상의 카테고리
    @GetMapping("/top")
    public String top(Model model) throws Exception {
        List<Product> topList = productService.top();

        for (Product product : topList) {
            // 상품 옵션 설정
            List<ProductOption> options = productService.getProductOptionsByProductId(product.getPNo());
            product.setOptions(options);

            // 상품 이미지 설정
            Files file = new Files();
            file.setParentNo(product.getPNo());
            file.setParentTable(product.getCategory());
            List<Files> productImages = fileService.listByParent(file);
            
            // 첫 번째 이미지 URL 설정
            if (!productImages.isEmpty()) {
                product.setImageUrl(productImages.get(0).getFilePath());
            } else {
                product.setImageUrl("/files/img?imgUrl=no-image.png"); // 기본 이미지 경로 설정
            }
        }

        model.addAttribute("topList", topList);
        return "/product/top";
    }

    // 하의 카테고리
    @GetMapping("/pants")
    public String pants(Model model) throws Exception {
        List<Product> pantsList = productService.pants();

        for (Product product : pantsList) {
            // 상품 옵션 설정
            List<ProductOption> options = productService.getProductOptionsByProductId(product.getPNo());
            product.setOptions(options);

            // 상품 이미지 설정
            Files file = new Files();
            file.setParentNo(product.getPNo());
            file.setParentTable(product.getCategory());
            List<Files> productImages = fileService.listByParent(file);
            
            // 첫 번째 이미지 URL 설정
            if (!productImages.isEmpty()) {
                product.setImageUrl(productImages.get(0).getFilePath());
            } else {
                product.setImageUrl("/files/img?imgUrl=no-image.png"); // 기본 이미지 경로 설정
            }
        }
        model.addAttribute("pantsList", pantsList);

        return "/product/pants";
    }

    // 신발 카테고리
    @GetMapping("/shoes")
    public String shoes(Model model) throws Exception {
        List<Product> shoesList = productService.shoes();
        for (Product product : shoesList) {
            // 상품 옵션 설정
            List<ProductOption> options = productService.getProductOptionsByProductId(product.getPNo());
            product.setOptions(options);

            // 상품 이미지 설정
            Files file = new Files();
            file.setParentNo(product.getPNo());
            file.setParentTable(product.getCategory());
            List<Files> productImages = fileService.listByParent(file);
            
            // 첫 번째 이미지 URL 설정
            if (!productImages.isEmpty()) {
                product.setImageUrl(productImages.get(0).getFilePath());
            } else {
                product.setImageUrl("/files/img?imgUrl=no-image.png"); // 기본 이미지 경로 설정
            }
        }
        model.addAttribute("shoesList", shoesList);

        return "/product/shoes";
    }

    // 악세사리 카테고리
    @GetMapping("/accessory")
    public String accessory(Model model) throws Exception {
        List<Product> accessoryList = productService.accessory();

        for (Product product : accessoryList) {
            // 상품 옵션 설정
            List<ProductOption> options = productService.getProductOptionsByProductId(product.getPNo());
            product.setOptions(options);

            // 상품 이미지 설정
            Files file = new Files();
            file.setParentNo(product.getPNo());
            file.setParentTable(product.getCategory());
            List<Files> productImages = fileService.listByParent(file);
            
            // 첫 번째 이미지 URL 설정
            if (!productImages.isEmpty()) {
                product.setImageUrl(productImages.get(0).getFilePath());
            } else {
                product.setImageUrl("/files/img?imgUrl=no-image.png"); // 기본 이미지 경로 설정
            }
        }
        model.addAttribute("accessoryList", accessoryList);

        return "/product/accessory";
    }
}
