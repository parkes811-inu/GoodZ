package com.springproject.goodz.product.controller;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.springproject.goodz.post.dto.Like;
import com.springproject.goodz.post.dto.Post;
import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.dto.ProductOption;
import com.springproject.goodz.product.service.ProductService;
import com.springproject.goodz.user.dto.Users;
import com.springproject.goodz.user.dto.Wish;
import com.springproject.goodz.user.service.WishListService;
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

    @Autowired
    private WishListService wishListService;

    // DecimalFormat 인스턴스 한 번 생성
    DecimalFormat decimalFormat = new DecimalFormat("#,### 원");

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

        model.addAttribute("productList", productList);
        return "/product/index";
    }

    @GetMapping("/{pNo}")
    public String viewProduct(@PathVariable int pNo, HttpServletRequest request, 
                            HttpServletResponse response, Model model) throws Exception {

        String cookieName = "viewedProduct_" + pNo;
        boolean alreadyViewed = false;

        // 쿠키 확인
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    alreadyViewed = true;
                    break;
                }
            }
        }

        if (!alreadyViewed) {
            // 조회수 증가 로직
            productService.updateViews(pNo);

            // 쿠키 설정 (공백 제거, 유효한 문자열 사용)
            Cookie viewCookie = new Cookie(cookieName, "true");
            viewCookie.setMaxAge((int) TimeUnit.DAYS.toSeconds(1)); // 쿠키 유효기간 1일
            viewCookie.setHttpOnly(true);
            response.addCookie(viewCookie);
        }

        // 제품 정보 모델에 추가
        Product product = productService.getProductBypNo(pNo);
        model.addAttribute("product", product);

        return "redirect:/product/detail/" + pNo; // 상품 상세 페이지로 리다이렉트
    }


    @GetMapping("/detail/product/size_table")
    public String productSizeInfoPage() {
        return "fragments/product/size_table";
    }

    @GetMapping("/detail/{pNo}")
    public String productDetailPage(@PathVariable("pNo") Integer pNo
                                  , Model model
                                  , HttpSession session) throws Exception {

        // 세션 정보 세팅
        Users loginUser = (Users)session.getAttribute("user");
        model.addAttribute("loginUser", loginUser);                   
        
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
        // 원화 형식으로 변환
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("ko", "KR"));
        DecimalFormat decimalFormat = (DecimalFormat) currencyFormatter;
        decimalFormat.applyPattern("#,### 원");
        String formattedMinPrice = currencyFormatter.format(minPrice);
        
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("formattedMinPrice", formattedMinPrice);

        model.addAttribute("product", product);
        model.addAttribute("options", options);
        model.addAttribute("images", images);

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

        // 사용자 관심 목록 확인
        boolean isWishlisted = false;
        if (loginUser != null) {
            // isWishlisted = wishListService.isWishlisted(loginUser.getUserId(), "product", pNo);
            Wish wish = new Wish();
            wish.setUserId(loginUser.getUserId());
            wish.setParentTable("product");
            wish.setParentNo(product.getPNo());
            isWishlisted = wishListService.listById(wish);
        }

        model.addAttribute("isWishlisted", isWishlisted);


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
        //int offset = Math.max(0, (page - 1) * size);
        int offset = page * size;

        // 쿼리 실행 후 결과 로그 출력
        List<Product> products = productService.findSameBrandProducts(brand, category, pNo, offset, size);

        for (Product product : products) {
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

        // 쿼리 결과를 로그로 확인
        System.out.println("쿼리 결과: " + products);

        return ResponseEntity.ok(products);
    }
}
