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
import com.springproject.goodz.product.dto.ProductOption;
import com.springproject.goodz.product.service.BrandService;
import com.springproject.goodz.product.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @GetMapping("")
    public String index() {
        return "/admin/index";
    }

    @GetMapping("/brands")
    public String brand_list(Model model) throws Exception {
        List<Brand> brandList = brandService.list();
        model.addAttribute("brandList", brandList);
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
    public String product_list(Model model) throws Exception {
        List<Product> productList = productService.list();
        model.addAttribute("productList", productList);
        return "/admin/product_list";
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
        RedirectAttributes redirectAttributes) throws Exception {

        log.info("::::::::::::::상품 등록 요청::::::::::::::");
        log.info(product.toString());

        // 상품 등록
        int pNo = productService.insert(product);

        // 옵션 등록
        for (int i = 0; i < sizes.size(); i++) {
            ProductOption option = new ProductOption();
            option.setPNo(pNo); // set the generated product number
            option.setSize(sizes.get(i));
            option.setOptionPrice(optionPrices.get(i));
            option.setStockQuantity(stockQuantities.get(i));
            option.setStatus(status.get(i));
            productService.insertProductOption(option);
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

    @GetMapping("/product/detail/{pNo}")
    public String getProductDetail(@PathVariable("pNo") int pNo, Model model) throws Exception {
        Product product = productService.getProductBypNo(pNo);
        if (product == null) {
            throw new Exception("Product not found");
        }
        model.addAttribute("product", product);
        return "/admin/product_detail";
    }
}
