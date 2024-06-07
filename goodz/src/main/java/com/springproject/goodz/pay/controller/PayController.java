package com.springproject.goodz.pay.controller;

import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
     * @param pNo 상품 번호
     * @param size 상품 사이즈
     * @param model 모델
     * @param session 세션
     * @return 결제 페이지
     * @throws Exception
     */
    @GetMapping("/buy")
    public String buy(@RequestParam("pNo") int pNo,
                      @RequestParam("size") String size,
                      Model model, HttpSession session) throws Exception {

        Users user = (Users) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        // 기본 배송지를 찾기 위한 로직
        Shippingaddress defaultAddress = null;
        List<Shippingaddress> addresses = userService.selectByUserId(user.getUserId());
        for (Shippingaddress address : addresses) {
            if (userService.isDefaultAddress(address.getAddressNo())) {
                defaultAddress = address;
                break;
            }
        }

        // 단일 상품 조회
        Product product = productService.getProductBypNo(pNo);
        List<ProductOption> options = productService.getProductOptionsByProductId(product.getPNo());
        product.setOptions(options);


        int purchasePrice = 0;
        int optionId = 0;
        for (ProductOption option : options) {
            if (option.getSize().equals(size)) {
                purchasePrice = option.getOptionPrice();
            }

            if (option.getPNo() == pNo && option.getSize().equals(size)) {
                optionId = option.getOptionId();
            }
        }

        // purchase 테이블에 user_id, p_no, optionPrice, option_id 등록
        String userId = user.getUserId();

        Purchase purchase = new Purchase();
        purchase.setUserId(userId);
        purchase.setPNo(pNo);
        purchase.setPurchasePrice(purchasePrice);
        purchase.setPaymentMethod("purchase");
        purchase.setPurchaseState("pending"); // 구매 상태를 pending으로 설정
        purchase.setOptionId(optionId);

        // 주문 등록
        int result = payService.savePurchase(purchase);
        int purchaseNo = purchase.getPurchaseNo();
        log.info(":::::::::::::::::::::::::::::::::::::::::::::");
        log.info("purchaseNo : " + purchaseNo);

        if (result == 0) {
            return "redirect:/product/detail/" + pNo;
        }

        return "redirect:/pay/buy/" + purchaseNo; // 상품 구매 페이지로 이동
    }


    /**
     * 결제 하기 
     * @param purchaseNo
     * @return
     * @throws Exception 
     */
    @GetMapping("/buy/{purchaseNo}")
    public String getMethodName(@PathVariable("purchaseNo") int purchaseNo
                               ,Model model
                               ,HttpSession session) throws Exception {
        // TODO: purchaseNo 확인
        log.info("purchaseNo : " + purchaseNo);
        
        Users user = (Users) session.getAttribute("user");
        // TODO : 나중에 시큐리티로 권한 처리할 것..
        if (user == null) {
            return "redirect:/user/login";
        }

        // userId 가져 와서 
        // Shippingaddress 조회
        Shippingaddress defaultAddress = null;
        List<Shippingaddress> addresses = userService.selectByUserId(user.getUserId());
        for (Shippingaddress address : addresses) {
            if (userService.isDefaultAddress(address.getAddressNo())) {
                defaultAddress = address;
                break;
            }
        }
        

        // purchaseNo 조회
        // - p_no,  option_id 꺼내옴
        Purchase purchase = payService.selectPurchase(purchaseNo);

        
        // Product 조회
        int pNo = purchase.getPNo();
        log.info("::::::::::::::::::::::::::::::::::::::::::");
        log.info("pNo : " + pNo);
        Product product = productService.getProductBypNo(pNo);
        
        // ProductOption 조회
        int optionId = purchase.getOptionId();
        log.info("::::::::::::::::::::::::::::::::::::::::::");
        log.info("optionId : " + optionId);
        ProductOption productOption = productService.getProductOptionByOptionId(optionId);

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
        model.addAttribute("size", productOption.getSize());
        model.addAttribute("image", productImages);
        model.addAttribute("price", purchase.getPurchasePrice());
        model.addAttribute("purchaseNo", purchaseNo); // purchaseNo를 모델에 추가
        model.addAttribute("optionId", optionId);

        // 기본 배송지가 있는지 여부를 모델에 추가
        model.addAttribute("defaultAddress", defaultAddress);
        model.addAttribute("hasAddress", !addresses.isEmpty());
        model.addAttribute("addresses", addresses);

        return "/pay/buy";
    }
    


    /**
     * 결제 성공 시 호출되는 메서드 (POST 요청)
     * @param purchaseNo 구매 번호
     * @param paymentKey 결제 키
     * @param orderId 주문 ID
     * @param amount 결제 금액
     * @return 결제 결과
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/buy")
    public String updatePurchase(@RequestParam("purchaseNo") int purchaseNo,
                                 @RequestParam("paymentKey") String paymentKey,
                                 @RequestParam("orderId") String orderId,
                                 @RequestParam("amount") int amount,
                                 @RequestParam("address") String address) throws Exception {
        log.info("updatePurchase 호출됨: purchaseNo={}, orderId={}, amount={}, address={}", purchaseNo, orderId, amount, address);

        Purchase purchase = new Purchase();
        purchase.setPurchaseNo(purchaseNo);
        purchase.setOrderId(orderId);
        purchase.setPurchaseState("paid");
        purchase.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        String decodedAddress = URLDecoder.decode(address, "UTF-8"); // URL 디코딩
        purchase.setAddress(decodedAddress);
        log.info("시발1----------------------------------------------\n" + decodedAddress);
        log.info("Purchase 객체 생성됨: {}", purchase);

        int result = payService.updatePurchase(purchase);
        if (result > 0) {
            log.info("구매 업데이트 성공");
            return "success";
        }
        log.info("구매 업데이트 실패");
        return "fail";
    }

    /**
     * 판매 페이지 화면
     * @param pNo 상품 번호
     * @param size 상품 사이즈
     * @param price 상품 가격
     * @param model 모델
     * @param session 세션
     * @return 판매 페이지
     * @throws Exception
     */
    @GetMapping("/sell/{p_no}")
    public String sell(@PathVariable("p_no") int pNo,
                       @RequestParam("size") String size,
                       @RequestParam("price") int price,
                       Model model, HttpSession session) throws Exception {

        Users user = (Users) session.getAttribute("user");
        if (user == null) {
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

    /**
     * 판매 등록 처리
     * @param productNo 상품 번호
     * @param courier 택배사
     * @param trackingNumber 운송장 번호
     * @param size 상품 사이즈
     * @param address 배송 주소
     * @param salePrice 판매 가격
     * @param session 세션
     * @param model 모델
     * @return 판매 등록 결과 페이지
     * @throws Exception
     */
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
            return "redirect:/pay/complete?type=sell"; // 성공 시 완료 페이지로 리다이렉트
        } else {
            model.addAttribute("errorMessage", "판매 정보를 저장하는 데 실패했습니다.");
            return "/pay/sell"; // 실패 시 판매 페이지로 리다이렉트
        }
    }

    /**
     * 결제 또는 판매 완료 페이지
     * @param type 완료 타입 (buy 또는 sell)
     * @param model 모델
     * @return 완료 페이지
     * @throws Exception 
     */
    @GetMapping("/complete")
    public String complete(@RequestParam("purchaseNo") int purchaseNo,
                           @RequestParam("paymentKey") String paymentKey,
                           @RequestParam("orderId") String orderId,
                           @RequestParam("amount") int amount,
                           @RequestParam("address") String address,
                           @RequestParam("type") String type, Model model) throws Exception {
        
        String decodedAddress = URLDecoder.decode(address, "UTF-8"); // URL 디코딩

        log.info("updatePurchase 호출됨: purchaseNo={}, orderId={}, amount={}", purchaseNo, orderId, amount);

        Purchase purchase = new Purchase();
        purchase.setPurchaseNo(purchaseNo);
        purchase.setOrderId(orderId);
        purchase.setPurchaseState("paid");
        purchase.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        purchase.setAddress(decodedAddress);

        log.info("Purchase 객체 생성됨: {}", purchase);

        int result = payService.updatePurchase(purchase);
        if (result > 0) {
            log.info("구매 업데이트 성공");
            model.addAttribute("type", type);
            
            Purchase optionId = payService.selectPurchase(purchaseNo);
            int cnt = productService.checkStockQuantity(optionId.getOptionId());

            if((cnt - 1) == 0) {
                productService.minusQuantityByProductId(optionId.getOptionId());
                productService.changeStatus(optionId.getOptionId());
            } else {
                productService.minusQuantityByProductId(optionId.getOptionId());
            }

            return "redirect:/pay/buy/complete/" + purchaseNo + "?type="+type;
        }
        log.info("구매 업데이트 실패");
        type = "fail";
        model.addAttribute("type", type);
        return "redirect:/pay/fail" + purchaseNo;
    }

    /**
     * 결제 완료 페이지
     * @param purchaseNo
     * @param type : buy
     * @param model
     * @return
     */
    @GetMapping("/buy/complete/{purchaseNo}")
    public String getMethodName(@PathVariable("purchaseNo") String purchaseNo
                               ,@RequestParam("type") String type
                               ,Model model) {
        model.addAttribute("type", type);
        return "/pay/complete";
    }
    
}
