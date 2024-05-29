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

    // @PostMapping("/findPW")
    // public ResponseEntity<String> findPw(@RequestBody Users user) {
    //     String name = user.getUsername();
    //     String birth = user.getBirth();
    //     String user_id = user.getUserId();

    //     try {
    //         String pw = userService.findPw(name , birth , user_id);
    //         if (pw != null) {
    //             return ResponseEntity.ok(pw);
    //         } else {
    //             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("비밀번호를 찾을 수 없습니다.");
    //         }
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러가 발생했습니다.");
    //     }
    // }
    @PostMapping("/findPW")
    public String findPw(Users user, RedirectAttributes redirectAttributes, Model model) throws Exception {
        log.info("찾으려는 유저: " + user);

        log.info("컨트롤러 진입");
        log.info(user.toString());

        Users findMan = userService.findPw(user.getUsername(), user.getBirth(), user.getUserId());

        if (findMan == null) {
            log.info(":::::::::::::::일치하는 회원 없음:::::::::::::::");
            // error 파라미터만 전달하고 값은 주지 않음
            redirectAttributes.addAttribute("error", "true");
            
            return "redirect:/user/findPW";
        }

        redirectAttributes.addFlashAttribute("findMan", findMan);
    
        return "redirect:/user/changePW"; // 리다이렉트 사용
    }


    @GetMapping("/changePW")
    public String changePW(Model model) {

        Users findMan = (Users) model.asMap().get("findMan");
        if (findMan == null) {
            return "redirect:/user/findPW";
        }

        model.addAttribute("findMan", findMan);

        return "/user/changePW";
    }


    @PostMapping("/changePW")
    public ResponseEntity<String> changePw(@RequestBody Map<String, String> request) {
        //String username = request.get("username");
        //String birth = request.get("birth");
        String newPw = request.get("password");
        String userId = request.get("userId");
        log.info(("asdfsdfsdf"));
        log.info(userId);

        try {
            int result = userService.changePw(newPw, userId);
            if (result > 0) {
                return ResponseEntity.ok("비밀번호가 변경되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("현재 비밀번호가 일치하지 않습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경에 실패했습니다.");
        }
    }
    
    // @PostMapping("/changePW")
    // public ResponseEntity<String> changePw(@RequestBody Map<String, String> request) {
    //     String currentPw = request.get("currentPw");
    //     String newPw = request.get("newPw");
    
    //     log.info("컨트롤러 진입");
    
    //     // 사용자 아이디를 요청에서 직접 가져옵니다.
    //     String username = request.get("userId");
    
    //     try {
    //         // userService의 changePw 메서드를 호출하여 비밀번호 변경을 시도합니다.
    //         int result = userService.changePw(username, currentPw, newPw);
    //         if (result > 0) {
    //             // 변경이 성공하면 성공 응답을 반환합니다.
    //             return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    //         } else {
    //             // 변경이 실패하면 실패 응답을 반환합니다.
    //             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("현재 비밀번호가 일치하지 않습니다.");
    //         }
    //     } catch (Exception e) {
    //         // 예외 발생 시에도 실패 응답을 반환합니다.
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경에 실패했습니다.");
    //     }
    // }
    

    // @PostMapping("/changePW")
    // public ResponseEntity<String> changePw(@RequestBody Map<String, String> request) {
    //     String currentPw = request.get("currentPw");
    //     String newPw = request.get("newPw");
    
    //     log.info("컨트롤러 진입");

    //     // 사용자 정보는 SecurityContextHolder에서 가져오도록 변경
    //     // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    //     String username = request.get("userId");

    //     try {
    //         int result = userService.changePw(username, currentPw, newPw);
    //         if (result > 0) {
    //             return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    //         } else {
    //             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("현재 비밀번호가 일치하지 않습니다.");
    //         }
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 변경에 실패했습니다.");
    //     }
    // }


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