package com.springproject.goodz.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.springproject.goodz.admin.service.AdminService;
import com.springproject.goodz.pay.dto.Purchase;
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

    @Autowired
    private AdminService adminService;
    
    @GetMapping("")
    public String index(Model model) throws Exception {

        List<Map<String, Object>> saleStateCounts = adminService.countUserSalesByState();
        Map<String, Integer> saleStateMap = saleStateCounts.stream()
            .collect(Collectors.toMap(
                count -> (String) count.get("sale_state"), 
                count -> ((Long) count.get("count")).intValue()
            ));
        
        model.addAttribute("saleStateCounts", saleStateMap);

        List<Map<String, Object>> purchaseStateCounts = adminService.countUserPurchaseByState();
        Map<String, Integer> purchaseStateMap = purchaseStateCounts.stream()
            .collect(Collectors.toMap(
                count -> (String) count.get("purchase_state"), 
                count -> ((Long) count.get("count")).intValue()
            ));
        model.addAttribute("purchaseStateCounts", purchaseStateMap);

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
            return "/common/error";
        }
        return "redirect:/admin/products";
    }


    /**
     * 매입 내역 화면 ( 유저가 판매 )
     * @param model
     * @param page
     * @param pageNumber
     * @return
     * @throws Exception
     */
    @GetMapping("/purchase_state")
    public String userSaleList(Model model, Page page,
                                @RequestParam(value = "page", defaultValue = "1") int pageNumber) throws Exception {
                                    
        page.setPage(pageNumber);
        int total = adminService.getTotalCount();
        page.setTotal(total);
        
        List<Map<String, Object>> saleList = adminService.userSaleList(page);
        model.addAttribute("saleList", saleList);

        List<Map<String, Object>> saleStateCounts = adminService.countUserSalesByState();
        Map<String, Integer> saleStateMap = saleStateCounts.stream()
            .collect(Collectors.toMap(
                count -> (String) count.get("sale_state"), 
                count -> ((Long) count.get("count")).intValue()
            ));
        model.addAttribute("saleStateCounts", saleStateMap);
        model.addAttribute("page", page);
        return "/admin/purchase_state";
    }

    /**
     * 유저가 판매한 번호를 기준으로 단일 조회
     * @param sNo
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/purchase/detail/{sNo}")
    public String purchaseDetail(@PathVariable("sNo") int sNo, Model model) throws Exception {
        Map<String, Object> saleDetail = adminService.userSale(sNo);
        model.addAttribute("saleDetail", saleDetail);
        return "/admin/purchase_detail";
    }

    /**
     * 유저의 단일 판매 내역 상태 변경
     * @param sNo
     * @param saleState
     * @return
     * @throws Exception
     */
    @PostMapping("/purchase/update")
    public String updateSaleState(@RequestParam("sNo") int sNo, @RequestParam("saleState") String saleState) throws Exception {
        // 현재 판매 내역 가져오기
        Map<String, Object> saleDetail = adminService.userSale(sNo);
        String currentSaleState = (String) saleDetail.get("saleState");

        // 디버깅: 현재 판매 상태와 변경할 판매 상태를 출력
        System.out.println("Current sale state: " + currentSaleState);
        System.out.println("New sale state: " + saleState);

        if (!"completed".equals(currentSaleState) && "completed".equals(saleState)) {
            int pNo = (int) saleDetail.get("productNo");
            int initialPrice = adminService.getInitialPrice(pNo);

            // 디버깅: productNo와 initialPrice를 출력
            System.out.println("Product No: " + pNo);
            System.out.println("Initial Price: " + initialPrice);

            adminService.handleProductOption(pNo, (String) saleDetail.get("size"), initialPrice);
        }

        adminService.updateUserSaleState(sNo, saleState);
        return "redirect:/admin/purchase_state";
    }

    /**
     * 거래 내역 
     * @param model
     * @param page
     * @param pageNumber
     * @return
     * @throws Exception
     */
    @GetMapping("/pay_history")
    public String pay_history(Model model, Page page,
                            @RequestParam(value = "page", defaultValue = "1") int pageNumber) throws Exception {
        page.setPage(pageNumber);
        int total = adminService.TotalCount();
        page.setTotal(total);

        List<Map<String, Object>> purchaseList = adminService.userPurchaseList(page);
        model.addAttribute("purchaseList", purchaseList);

        List<Map<String, Object>> purchaseStateCounts = adminService.countUserPurchaseByState();
        Map<String, Integer> purchaseStateMap = purchaseStateCounts.stream()
            .collect(Collectors.toMap(
                count -> (String) count.get("purchase_state"), 
                count -> ((Long) count.get("count")).intValue()
            ));
        model.addAttribute("purchaseStateCounts", purchaseStateMap);

        model.addAttribute("page", page);
        return "/admin/pay_history";
    }

    /**
     * 유저가 구매한 상품 단일 조회
     * @param purchaseNo
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/pay_history/detail/{purchaseNo}")
    public String payhistorydetail(@PathVariable("purchaseNo") int purchaseNo, Model model) throws Exception {
        Purchase purchase = adminService.userPurchase(purchaseNo);
        model.addAttribute("purchase", purchase);
        return "/admin/pay_history_detail";
    }

    /**
     * 유저가 구매한 상품 상태 변경
     * @param purchaseNo
     * @param trackingNo
     * @param courier
     * @param purchaseState
     * @param pNo
     * @param size
     * @return
     * @throws Exception
     */
    @PostMapping("/pay_history/update")
    public String updatePurchase(@RequestParam("purchaseNo") int purchaseNo,
                                 @RequestParam(value = "trackingNumber", required = false) String trackingNumber,
                                 @RequestParam(value = "courier", required = false) String courier,
                                 @RequestParam("purchaseState") String purchaseState,
                                 @RequestParam("pNo") int pNo,
                                 @RequestParam("size") String size) throws Exception {
        // 현재 상태 조회
        Purchase currentPurchase = adminService.userPurchase(purchaseNo);
        String currentState = currentPurchase.getPurchaseState();

        // 운송장 번호가 제공된 경우 업데이트
        if (trackingNumber != null && !trackingNumber.isEmpty() && courier != null && !courier.isEmpty()) {
            String trackingNo = courier + ":" + trackingNumber;
            adminService.updateTrackingInfo(purchaseNo, trackingNo);
        }

        // 구매 상태 업데이트 및 재고 조정
        adminService.updatePurchaseState(purchaseNo, purchaseState, currentState, pNo, size);

        return "redirect:/admin/pay_history";
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
