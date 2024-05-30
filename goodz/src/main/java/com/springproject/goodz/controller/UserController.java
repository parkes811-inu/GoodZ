package com.springproject.goodz.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.springproject.goodz.user.dto.Users;
import com.springproject.goodz.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequestMapping("/user")
@SessionAttributes("findMan")  // 이상하면 지우자
public class UserController {
    
   
    @Autowired
    private UserService userService;

    

    @GetMapping("")
    public String index() {
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

    @PostMapping("/findPW")
    public String findPw(Users user, RedirectAttributes redirectAttributes, Model model) throws Exception {

        Users findMan = userService.findPw(user.getUsername(), user.getBirth(), user.getUserId());
        
       // 비밀번호 찾기에 성공했을 경우
        if (findMan != null) {
        // 세션 플래시 속성에 찾은 사용자 정보를 추가합니다.
        redirectAttributes.addFlashAttribute("findMan", findMan);

        // 리다이렉트하여 비밀번호 변경 페이지로 이동합니다.
        return "redirect:/user/changePW";
       }
        // 비밀번호 찾기에 실패했을 경우
        else {
        // 리다이렉트 시 error 파라미터를 추가하여 실패했음을 알립니다.
        redirectAttributes.addAttribute("error", "true");

        // 비밀번호 찾기 페이지로 리다이렉트합니다.
        return "redirect:/user/findPW";
       }
    }


    @GetMapping("/changePW")
    public String changePW(Model model) {
        // 모델에서 findMan 속성을 가져옵니다.
        Users findMan = (Users) model.asMap().get("findMan");

        // 만약 findMan이 null인 경우, 비밀번호 찾기 페이지로 리다이렉트합니다.
        if (findMan == null) {
            return "redirect:/user/findPW";
        }

        // 모델에 findMan 속성을 추가합니다.
        model.addAttribute("findMan", findMan);

        // 비밀번호 변경 페이지로 이동합
        return "/user/changePW";
    }


    @PostMapping("/changePW")
    public String changePw(@RequestParam("password") String newPassword,
                           @RequestParam("userId") String userId,
                           RedirectAttributes redirectAttributes, 
                           @ModelAttribute("findMan") Users findMan) {

            // findMan이 null이거나 사용자 ID가 일치하지 않는 경우, 비밀번호 찾기 페이지로 리다이렉트합니다.
            if (findMan == null || !findMan.getUserId().equals(userId)) {
                return "redirect:/user/findPW";
            }          

            try {
                // 비밀번호 변경 시도를 로그로 기록합니다
                log.info("비밀번호 변경 시도: userId={}, newPassword={}", userId, newPassword);

                // 비밀번호를 변경하고 결과를 받아옵니다.
                int result = userService.changePw(newPassword, userId);

                if (result > 0) {
                    // 성공 메시지를 플래시 속성에 추가하고 로그인 페이지로 리다이렉트합니다.
                    redirectAttributes.addFlashAttribute("message", "Password successfully changed.");
                    return "redirect:/user/login";

                } else {
                    // 실패 메시지를 플래시 속성에 추가하고 비밀번호 변경 페이지로 리다이렉트합니다.
                    redirectAttributes.addFlashAttribute("error", "Password change failed.");
                    return "redirect:/user/changePW";
                }
            } catch (Exception e) {
                
                // 비밀번호 변경 중 오류가 발생한 경우, 오류 메시지를 플래시 속성에 추가하고 비밀번호 변경 페이지로 리다이렉트합니다.
                log.error("비밀번호 변경 중 오류 발생", e);
                redirectAttributes.addFlashAttribute("error", "An error occurred. Please try again.");
                return "redirect:/user/changePW";
            }
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