package com.springproject.goodz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springproject.goodz.user.service.UserService;

import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;



@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
    
    @GetMapping("")
    public String index() {
        return "/user/index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }
    
    @GetMapping("/signup")
    public String signup() {
        return "/user/signup";
    }

    @GetMapping("/signup2")
    public String signup2() {
        return "/user/signup2";
    }

    @GetMapping("/findID")
    public String findID() {
        return "/user/findID";
    }

    @GetMapping("/findPW")
    public String findPW() {
        return "/user/findPW";
    }

    @GetMapping("/purchase")
    public String purchase() {
        return "/user/purchase";
    }

    @GetMapping("/sales")
    public String sales() {
        return "/user/sales";
    }

    // 관심페이지 이동 _ 디폴트: 상품
    @GetMapping("/wishlist/products")
    public String wishlist_products() {
        return "/user/wishlist_products";
    }

    // 관심페이지 이동 _ 스타일
    @GetMapping("/wishlist/styles")
    public String wishlist_styles() {
        return "/user/wishlist_styles";
    }

    @GetMapping("/manage_info")
    public String manage_info() {
        return "/user/manage_info";
    }

    @GetMapping("/address")
    public String address() {
        return "/user/address";
    }

    @GetMapping("/account")
    public String account() {
        return "/user/account";
    }

    @GetMapping("/style_profile")
    public String style_profile() {
        return "/user/style_profile";
    }
    

}
