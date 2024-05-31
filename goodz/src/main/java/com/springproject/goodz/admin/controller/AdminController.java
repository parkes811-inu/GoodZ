package com.springproject.goodz.admin.controller;

import java.util.List;

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

import com.springproject.goodz.product.dto.Brand;
import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.service.BrandService;
import com.springproject.goodz.product.service.ProductService;

import lombok.extern.slf4j.Slf4j;


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

    @Autowired
    private ProductService productService;
    
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

    /**
     * 브랜드 등록 처리
     * @param brand
     * @return
     * @throws Exception
     */
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
    


    // @PostMapping("/add_brand")
    // public String addBrand(Brand brand) {
        
    //     return entity;
    // }
    

    /**
     * 관리자 상품 목록 화면
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/products")
    public String product_list(Model model) throws Exception{
        List<Product> productList = productService.list();

        model.addAttribute("productList", productList);

        return "/admin/product_list";
    }

    /**
     * 상품 등록 화면
     * @return
     */
    @GetMapping("/add_product")
    public String moveToAddProduct(Model model) throws Exception {
        List<Brand> brandList = brandService.list();
        model.addAttribute("brandList", brandList);
        return "/admin/add_product";
    }

    @PostMapping("/products")
    public String addProducts(@ModelAttribute Product product, 
                            @RequestParam("productFiles") List<MultipartFile> productFiles, 
                            RedirectAttributes redirectAttributes) throws Exception {
        log.info("::::::::::::::상품 등록 요청::::::::::::::");
        log.info(product.toString());

        // 상품 객체에 파일 목록 설정
        // product.setProductFiles(productFiles);
        
        int result = productService.insert(product);

        if (result == 0) {
            log.info("::::::::::::::상품 등록 처리 중 예외발생::::::::::::::");
            return "redirect:/admin/add_product";
        }

        return "redirect:/admin/products";
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

    // 사이즈, 색상 테이블을 따로 만들어서 저장해야된다. 은서야
    // 그래야 제품 번호에 매핑되어서 제품 이름 클릭 시 
    // 해당 사이즈, 색상 들 좌라라락 떠서 해당하는 사이즈들 넣고, 빼고, 할 수 있다.
    @GetMapping("/product/detail/{pNo}")
    public String getProductDetail(@PathVariable("pNo") int pNo, Model model) throws Exception {
        // 상품 이름을 사용하여 상품 정보를 조회합니다.
        Product product = productService.getProductBypNo(pNo);
        if (product == null) {
            throw new Exception("Product not found");
        }
        model.addAttribute("product", product);
        return "/admin/product_detail"; // 상세 페이지 템플릿 이름
    }


    // @GetMapping("/product/detail")
    // public String product_detail(@RequestParam("productName") String productName, Model model) throws Exception {
    //     // 상품 이름을 사용하여 상품 정보를 조회합니다.
    //     Product product = productService.getProductBypName(productName);
    //     model.addAttribute("product", product);
    //     return "/admin/product/detail"; // 상세 페이지 템플릿 이름
    // }

    

    
}
