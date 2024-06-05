package com.springproject.goodz.pay.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springproject.goodz.pay.dto.Purchase;
import com.springproject.goodz.pay.dto.Sales;
import com.springproject.goodz.pay.service.PayService;
import com.springproject.goodz.product.dto.Product;
import com.springproject.goodz.product.dto.ProductOption;
import com.springproject.goodz.product.service.ProductService;
import com.springproject.goodz.user.dto.Shippingaddress;
import com.springproject.goodz.user.dto.Users;
import com.springproject.goodz.user.service.UserService;
import com.springproject.goodz.utils.dto.Files;
import com.springproject.goodz.utils.service.FileService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @Autowired
    private PayService payService;

    /**
     * 결제 페이지 화면 
     * @param model
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping("/buy/{p_no}")
    public String buy(@PathVariable("p_no") int pNo,
                    @RequestParam("size") String size,
                    Model model, HttpSession session) throws Exception {

        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        // 단일 상품 조회
        Product product = productService.getProductBypNo(pNo);
        List<ProductOption> options = productService.getProductOptionsByProductId(product.getPNo());
        product.setOptions(options);

        // 기본 배송지를 찾기 위한 로직
        Shippingaddress defaultAddress = null;
        List<Shippingaddress> addresses = userService.selectByUserId(user.getUserId());
        for (Shippingaddress address : addresses) {
            if (userService.isDefaultAddress(address.getAddressNo())) {
                defaultAddress = address;
                break;
            }
        }

        int price = 0;
        for (ProductOption option : options) {
            if (option.getSize().equals(size)) {
                price = option.getOptionPrice();
                break; // 일치하는 사이즈를 찾으면 반복문을 종료
            }
        }

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

        model.addAttribute("product", product); // 모델에 상품 정보를 추가합니다.
        model.addAttribute("size", size);
        model.addAttribute("image", productImages);
        model.addAttribute("price", price);
        // 기본 배송지가 있는지 여부를 모델에 추가
        model.addAttribute("defaultAddress", defaultAddress);
        model.addAttribute("hasAddress", !addresses.isEmpty());
        model.addAttribute("addresses", addresses);
        
        return "/pay/buy"; // 상품 구매 페이지로 이동합니다.
    }

    

    /**
     * 판매 페이지 화면
     * @param pNo
     * @param size
     * @param price
     * @param model
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping("/sell/{p_no}")
    public String sell(@PathVariable("p_no") int pNo,
                    @RequestParam("size") String size,
                    @RequestParam("price") int price,
                    Model model, HttpSession session) throws Exception{
        
        Users user = (Users) session.getAttribute("user");
        if(user == null){
            return "redirect:/user/login";
        }

        // 단일 상품 조회
        Product product = productService.getProductBypNo(pNo);

        // 기본 배송지를 찾기 위한 로직
        Shippingaddress defaultAddress = null;
        List<Shippingaddress> addresses = userService.selectByUserId(user.getUserId());
        for (Shippingaddress address : addresses) {
            if (userService.isDefaultAddress(address.getAddressNo())) {
                defaultAddress = address;
                break;
            }
        }

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

        model.addAttribute("product", product); // 모델에 상품 정보를 추가합니다.
        model.addAttribute("size", size);
        model.addAttribute("image", productImages);

        // 기본 배송지가 있는지 여부를 모델에 추가
        model.addAttribute("defaultAddress", defaultAddress);
        model.addAttribute("hasAddress", !addresses.isEmpty());
        model.addAttribute("addresses", addresses);

        // 가격 정보 추가
        model.addAttribute("price", price); // 선택된 가격
        model.addAttribute("initialPrice", product.getInitialPrice()); // 초기 가격 추가

        return "/pay/sell";
    }

    @PostMapping("/sell")
    public String insertSale(@RequestParam("productNo") int productNo,
                             @RequestParam("courier") String courier,
                             @RequestParam("trackingNumber") String trackingNumber,
                             @RequestParam("size") String size,
                             @RequestParam("address") String address,
                             @RequestParam("salePrice") int salePrice,
                             HttpSession session, Model model) throws Exception {

        // 사용자 세션에서 사용자 정보 가져오기
        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        // Sales 객체 생성 및 데이터 설정
        Sales sales = new Sales();
        sales.setUserId(user.getUserId()); // 사용자 ID 설정
        sales.setPNo(productNo); // 상품 번호 설정
        sales.setSize(size); // 사이즈 설정
        // 택배사와 운송장 번호를 결합하여 하나의 컬럼에 저장
        sales.setSaleTrackingNo(courier + " - " + trackingNumber);
        sales.setAddress(address); // 주소 설정
        sales.setSalePrice(salePrice); // 판매 가격 설정
        sales.setSaleState("pending"); // 판매 상태 설정

        // 판매 정보 데이터베이스에 저장
        int result = payService.insertSale(sales);

        // 저장 결과 처리
        if (result > 0) {
            return "redirect:/pay/complete"; // 성공 시 완료 페이지로 리다이렉트
        } else {
            model.addAttribute("errorMessage", "판매 정보를 저장하는 데 실패했습니다.");
            return "sell"; // 실패 시 판매 페이지로 리다이렉트
        }
    }


    
    @GetMapping("/success")
    public String success() {
        return "/pay/success";
    }

    @GetMapping("/fail")
    public String fail() {
        return "/pay/fail";
    }

    @GetMapping("/complete")
    public String complete() {
        return "/pay/complete";
    }

    

    /**
     * 성공시 그 내역들 불러오기
     * @param params
     * @param model
     * @return
     */
    @PostMapping("/success")
    public String handleSuccess(@RequestParam Map<String, String> params, Model model) {
        // 성공 처리 로직 추가
        model.addAttribute("params", params);
        return "pay/success";
    }

    /**
     * 실패시 그 내역을 불러오기
     * @param params
     * @param model
     * @return
     */
    @PostMapping("/fail")
    public String handleFail(@RequestParam Map<String, String> params, Model model) {
        // 실패 처리 로직 추가
        model.addAttribute("params", params);
        return "pay/fail";
    }
}

