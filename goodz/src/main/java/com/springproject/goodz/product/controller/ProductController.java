package com.springproject.goodz.product.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

            // 최저 가격 계산
            if (!options.isEmpty()) {
                int minPrice = options.stream()
                                    .mapToInt(ProductOption::getOptionPrice)
                                    .min()
                                    .orElse(0);
                product.setMinPrice(minPrice);
            } else {
                product.setMinPrice(product.getInitialPrice()); // 옵션이 없는 경우 기본 가격 설정
            }

            // 첫 번째 이미지 URL 설정
            if (!productImages.isEmpty()) {
                product.setImageUrl(productImages.get(0).getFilePath());
            } else {
                product.setImageUrl("/files/img?imgUrl=no-image.png"); // 기본 이미지 경로 설정
            }

            // 디버깅을 위해 minPrice 값 출력
            System.out.println("Product ID: " + product.getPNo() + ", Min Price: " + product.getMinPrice());
        }

        model.addAttribute("productList", productList);
        return "/product/index";
    }


    @GetMapping("/detail/product/size_table")
    public String productSizeInfoPage() {
        return "fragments/product/size_table";
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
        
        String category = product.getCategory();
        String brand = product.getBName();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> sizeMap = new HashMap<>();

        if (category.equals("shoes")) {
            sizeMap.put("sizes", new int[]{220, 230, 240, 250, 260, 270, 280});
        } else if (category.equals("top") || category.equals("pants")) {
            sizeMap.put("sizes", new String[]{"S", "M", "L"});
        } else {
            // accessory인 경우
            sizeMap.put("sizes", "free");
        }

        String sizeJson = objectMapper.writeValueAsString(sizeMap);
        model.addAttribute("sizeJson", sizeJson);

       // List<Product> brandProducts = productService.newArrivals();
       //List<Product> brandProducts = productService.findSameBrandProducts(brand, category, pNo); 

        // productService.findSameBrandProducts(product.getCategory(), product.getBName()); 
        // for (Product brandProduct : brandProducts) {
        //     // 상품 옵션 설정
        //     List<ProductOption> options2 = productService.getProductOptionsByProductId(product.getPNo());
        //     brandProduct.setOptions(options2);

        //     // 상품 이미지 설정
        //     Files file2 = new Files();
        //     file2.setParentNo(brandProduct.getPNo());
        //     file2.setParentTable(brandProduct.getCategory());
        //     List<Files> productImages2 = fileService.listByParent(file2);
            
        //     // 최저 가격 계산
        //     if (!options.isEmpty()) {
        //         int minPrice2 = options.stream()
        //                             .mapToInt(ProductOption::getOptionPrice)
        //                             .min()
        //                             .orElse(0);
        //     brandProduct.setMinPrice(minPrice);
        //     } else {
        //         brandProduct.setMinPrice(brandProduct.getInitialPrice()); // 옵션이 없는 경우 기본 가격 설정
        //     }
            
        //     // 첫 번째 이미지 URL 설정
        //     if (!productImages2.isEmpty()) {
        //         brandProduct.setImageUrl(productImages2.get(0).getFilePath());
        //     } else {
        //         brandProduct.setImageUrl("/files/img?imgUrl=no-image.png"); // 기본 이미지 경로 설정
        //     }
        // }

        // model.addAttribute("brandProducts", brandProducts);

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
            
            // 최저 가격 계산
            if (!options.isEmpty()) {
                int minPrice = options.stream()
                                    .mapToInt(ProductOption::getOptionPrice)
                                    .min()
                                    .orElse(0);
                product.setMinPrice(minPrice);
            } else {
                product.setMinPrice(product.getInitialPrice()); // 옵션이 없는 경우 기본 가격 설정
            }
            
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
            
            // 최저 가격 계산
            if (!options.isEmpty()) {
                int minPrice = options.stream()
                                    .mapToInt(ProductOption::getOptionPrice)
                                    .min()
                                    .orElse(0);
                product.setMinPrice(minPrice);
            } else {
                product.setMinPrice(product.getInitialPrice()); // 옵션이 없는 경우 기본 가격 설정
            }

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
            
            // 최저 가격 계산
            if (!options.isEmpty()) {
                int minPrice = options.stream()
                                    .mapToInt(ProductOption::getOptionPrice)
                                    .min()
                                    .orElse(0);
                product.setMinPrice(minPrice);
            } else {
                product.setMinPrice(product.getInitialPrice()); // 옵션이 없는 경우 기본 가격 설정
            }

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
            
            // 최저 가격 계산
            if (!options.isEmpty()) {
                int minPrice = options.stream()
                                    .mapToInt(ProductOption::getOptionPrice)
                                    .min()
                                    .orElse(0);
                product.setMinPrice(minPrice);
            } else {
                product.setMinPrice(product.getInitialPrice()); // 옵션이 없는 경우 기본 가격 설정
            }
            
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


    // 인피니티 스크롤을 위한 컨트롤러
    @GetMapping("/brand/products")
    public ResponseEntity<List<Product>> getBrandProducts(@RequestParam("page") int page, 
                                                        @RequestParam("size") int size,
                                                        @RequestParam("brand") String brand,
                                                        @RequestParam("category") String category,
                                                        @RequestParam("pNo") int pNo) throws Exception {
        // 페이지 번호와 사이즈에 따라 오프셋(offset) 계산
        int offset = (page - 1) * size;
        
        // 페이지 번호와 사이즈를 이용하여 해당 브랜드의 상품 목록을 조회
        List<Product> products = productService.findSameBrandProducts(brand, category, pNo, offset, size);
        
        return ResponseEntity.ok(products);
    }
}
