package com.springproject.goodz.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mysql.cj.protocol.ExportControlled;
import com.springproject.goodz.product.dto.Brand;
import com.springproject.goodz.product.dto.Page;
import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.dto.ProductOption;
import com.springproject.goodz.product.dto.UpdateProductRequest;
import com.springproject.goodz.product.service.BrandService;
import com.springproject.goodz.product.service.ProductService;
import com.springproject.goodz.utils.dto.Files;
import com.springproject.goodz.utils.service.FileService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;
    
    @GetMapping("")
    public String index() {
        return "/admin/index";
    }

    @GetMapping("/brands")
    public String brandList(Model model, Page page,
                            @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                            @RequestParam(value = "keyword", defaultValue = "") String keyword) throws Exception {
        // 페이지 번호 설정
        page.setPage(pageNumber);

        // 전체 데이터 개수 설정
        int total = brandService.getTotalCount(keyword);
        page.setTotal(total);

        // 데이터 요청
        List<Brand> brandList = brandService.brandList(page, keyword);

        // 페이징
        log.info("page : " + page);
        // 검색
        log.info("keyword : " + keyword);

        // 모델 등록
        model.addAttribute("brandList", brandList);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);

        // 뷰 페이지 지정
        return "/admin/brand_list";  
    }

    @GetMapping("/add_brand")
    public String moveToAddBrand() {
        return "/admin/add_brand";
    }

    @PostMapping("/brands")
    public String addBrands(Brand brand) throws Exception {
        log.info("::::::::::::::브랜드 등록 요청::::::::::::::");
        log.info(brand.toString());

        int result = brandService.insert(brand);
        if (result == 0) {
            log.info("::::::::::::::브랜드 등록 처리 중 예외발생::::::::::::::");
            return "/admin/add_brand";
        }
        return "redirect:/admin/brands";
    }

    @GetMapping("/products")
    public String productList(Model model, Page page,
                          @RequestParam(value = "page", defaultValue = "1") int pageNumber,
                          @RequestParam(value = "keyword", defaultValue = "") String keyword) throws Exception {
        // 페이지 번호 설정
        page.setPage(pageNumber);

        // 전체 데이터 개수 설정
        int total = productService.getTotalCount(keyword);
        page.setTotal(total);

        // 데이터 요청
        List<Product> productList = productService.productList(page, keyword);

        // 페이징
        log.info("page : " + page);
        // 검색
        log.info("keyword : " + keyword);

        // 모델 등록
        model.addAttribute("productList", productList);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);

        // 뷰 페이지 지정
        return "admin/product_list";
    }

    @GetMapping("/add_product")
    public String moveToAddProduct(Model model) throws Exception {
        List<Brand> brandList = brandService.list();
        model.addAttribute("brandList", brandList);
        return "/admin/add_product";
    }

    @PostMapping("/products")
    public String addProducts(
        @ModelAttribute Product product, 
        @RequestParam("productFiles") List<MultipartFile> productFiles, 
        @RequestParam("sizes") List<String> sizes, 
        @RequestParam("optionPrices") List<Integer> optionPrices, 
        @RequestParam("stockQuantities") List<Integer> stockQuantities, 
        @RequestParam("status") List<String> status,
        @RequestParam("category") String category,
        @RequestParam("price") int price, 
        @RequestParam(value = "mainImgIndex", required = false, defaultValue = "-1") int mainImgIndex,
        RedirectAttributes redirectAttributes) throws Exception {

        log.info("::::::::::::::상품 등록 요청::::::::::::::");
        log.info(product.toString());
        
        product.setCategory(category);
        product.setInitialPrice(price);
        product.setProductFiles(productFiles); // 파일 리스트를 설정합니다.

        int result = productService.insert(product, mainImgIndex);
        
        if(result > 0) {
            int pNo = product.getPNo();
            // 옵션 등록
            for (int i = 0; i < sizes.size(); i++) {
                ProductOption option = new ProductOption();
                option.setPNo(pNo); // set the generated product number
                option.setSize(sizes.get(i));
                option.setOptionPrice(optionPrices.get(i));
                option.setStockQuantity(stockQuantities.get(i));
                option.setStatus(status.get(i));
                productService.insertProductOption(option);

                // priceHistory에 사이즈 별 가격 최초 등록
                productService.makeHistory(pNo, sizes.get(i), optionPrices.get(i));
            }

        } else {
            // error 페이지 만드러야 댐.....
            return "/common/error";
        }
        return "redirect:/admin/products";
    }


    @GetMapping("/purchase_state")
    public String purchase_state() {
        return "/admin/purchase_state";
    }

    @GetMapping("/purchase/detail")
    public String purchase_detail() {

        // if (reuslt > 0 && status == 정산완료) {
        //     int sival = productService.plusSize(1, S);
        //     update id="plusSize"
        //     update product_option set stock_quantity = stock_quantity + 1 where p_no = 1 and size = 'S';
        // }
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

    @GetMapping("/product/detail/{pNo}")
    public String getProductDetail(@PathVariable("pNo") int pNo, Model model) throws Exception {
        Product product = productService.getProductBypNo(pNo);
        List<ProductOption> option =  productService.adminOptionsByProductId(pNo);
        Files file = new Files();
        file.setParentNo(pNo);
        file.setParentTable(product.getCategory());
        List<Files> images = fileService.listByParent(file);

        List<Brand> brandList = brandService.list();
        model.addAttribute("brandList", brandList);

        model.addAttribute("product", product);
        model.addAttribute("option", option);
        model.addAttribute("images", images);

        return "/admin/product_detail";
    }

    @PostMapping("/updateProduct")
    public String updateProduct(@ModelAttribute UpdateProductRequest updateProductRequest, @RequestParam Map<String, String> params) throws Exception {
        log.info("Received Update Request: {}", updateProductRequest);

        Product product = new Product();
        product.setPNo(updateProductRequest.getPNo());
        product.setProductName(updateProductRequest.getProductName());
        product.setInitialPrice(updateProductRequest.getInitialPrice());
        product.setBName(updateProductRequest.getBName());
        product.setCategory(updateProductRequest.getCategory());

        List<ProductOption> options = new ArrayList<>();
        int index = 0;
        while (params.containsKey("optionIds[" + index + "]")) {
            ProductOption option = new ProductOption();
            option.setOptionId(Integer.parseInt(params.get("optionIds[" + index + "]")));
            option.setPNo(Integer.parseInt(params.get("optionPNos[" + index + "]")));
            option.setSize(params.get("sizes[" + index + "]"));
            option.setOptionPrice(Integer.parseInt(params.get("optionPrices[" + index + "]")));
            
            String addedStockQuantityStr = params.get("addedStockQuantities[" + index + "]");
            if (addedStockQuantityStr != null && !addedStockQuantityStr.isEmpty()) {
                option.setAddedStockQuantity(Integer.parseInt(addedStockQuantityStr));
            } else {
                option.setAddedStockQuantity(0); // 기본값 설정
            }
            
            option.setStatus(params.get("status[" + index + "]"));
            options.add(option);
            index++;
        }
        product.setOptions(options);

        log.info("Product options: {}", product.getOptions());

        productService.updateProduct(product);

        return "redirect:/admin/product/detail/" + updateProductRequest.getPNo();
    }
    

}
