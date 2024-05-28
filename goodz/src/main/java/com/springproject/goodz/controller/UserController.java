package com.springproject.goodz.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.springproject.goodz.user.dto.Users;
import com.springproject.goodz.user.service.UserService;

import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping("")
    public String index(Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Users user = userService.findUserByUsername(currentUserName);

        model.addAttribute("user", user);
        return "/user/index";
    }

    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new Users());
        return "/user/signup";
    }

    @PostMapping("/signup")
    public ModelAndView postUserInfo(@ModelAttribute Users user) {
        // 데이터를 가지고 signup2.html로 이동
        ModelAndView modelAndView = new ModelAndView("/user/signup2");
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    @PostMapping("/checkId")
    public ResponseEntity<String> checkDuplicate(@RequestBody Map<String, String> request) throws Exception {
        String userId = request.get("userId");
        boolean isAvailable = userService.checkId(userId);
        if (isAvailable) {
            return ResponseEntity.ok("사용 가능한 아이디입니다.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 아이디입니다.");
        }
    }

    @PostMapping("/signup2")
    public ResponseEntity<String> signUp(@RequestBody Users user) throws Exception {
        // 회원 가입 처리 로직
        userService.join(user);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

    @GetMapping("/findID")
    public String findID() {
        return "/user/findID";
    }

    @PostMapping("/findID")
    public ResponseEntity<String> findId(@RequestBody Users user) {
        String phone = user.getPhoneNumber();
        String name = user.getUsername();

        try {
            String id = userService.findId(phone, name);
            if (id != null) {
                return ResponseEntity.ok(id);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아이디를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러가 발생했습니다.");
        }
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
    public String wishlist_products(Model model) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            Users user = userService.findUserByUsername(userDetails.getUsername());
            model.addAttribute("user", user);
        }
        return "/user/wishlist_products";
    }

    // 관심페이지 이동 _ 스타일
    @GetMapping("/wishlist/styles")
    public String wishlist_styles() {
        return "/user/wishlist_styles";
    }

    @GetMapping("/manage_info")
    public String manage_info(Model model) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        Users user = userService.findUserByUsername(currentUserName);

        model.addAttribute("user", user);
        return "/user/manage_info";
    }

    @GetMapping("/address")
    public String address() {
        return "/user/address";
    }

    @GetMapping("/add_address")
    public String add_address() {
        return "/user/add_address";
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
