package com.springproject.goodz.controller;

import java.text.DecimalFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springproject.goodz.post.dto.Post;
import com.springproject.goodz.post.service.PostService;
import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.dto.ProductOption;
import com.springproject.goodz.product.service.ProductService;
import com.springproject.goodz.utils.dto.Files;
import com.springproject.goodz.utils.service.FileService;

import lombok.extern.slf4j.Slf4j;




@Slf4j
@Controller
@RequestMapping("")
public class MainController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private FileService fileService;

    @Autowired
    private PostService postService;

    // DecimalFormat 인스턴스 한 번 생성
    DecimalFormat decimalFormat = new DecimalFormat("#,### 원");

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

        // 👔 최근 입고 제품
        for (Product product : newArrivalsList) {
            // 상품 옵션 설정
            List<ProductOption> options = productService.getProductOptionsByProductId(product.getPNo());
            product.setOptions(options);

            // 상품 이미지 설정
            Files file = new Files();
            file.setParentNo(product.getPNo());
            file.setParentTable(product.getCategory());
            List<Files> productImages = fileService.listByParent(file);

            // 최저 가격 계산
            if (!options.isEmpty()) {
                int minPrice = options.stream()
                                    .mapToInt(ProductOption::getOptionPrice)
                                    .min()
                                    .orElse(0);
                // 원화 형식으로 변환
                String formattedMinPrice = decimalFormat.format(minPrice);
                product.setFormattedMinPrice(formattedMinPrice);
            } else {
                // 옵션이 없는 경우 기본 가격 설정 및 형식 변환
                int initialPrice = product.getInitialPrice();
                String formattedMinPrice = decimalFormat.format(initialPrice);
                product.setFormattedMinPrice(formattedMinPrice);
            }


            // 첫 번째 이미지 URL 설정
            if (!productImages.isEmpty()) {
                product.setImageUrl(productImages.get(0).getFilePath());
            } else {
                product.setImageUrl("/files/img?imgUrl=no-image.png"); // 기본 이미지 경로 설정
            }
        }
        model.addAttribute("newArrivalsList", newArrivalsList);

        // 📄인기게시글 4개
        List<Post> popularPosts = postService.popularPosts();
        model.addAttribute("popularPosts", popularPosts);

        return "/index";
    }




}