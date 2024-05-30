package com.springproject.goodz.post.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.springproject.goodz.user.dto.Users;
import com.springproject.goodz.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*
 * 스타일 게시글
 * [GET]    /styles                  게시글 목록
 * [GET]    /styles/게시글번호       게시글 조회
 * [GET]    /styles/update           게시글 수정페이지
 * [GET]    /styles/insert           게시글 수정페이지
 * [POST]   /styles                  게시글 작성처리
 * [DELETE] /styles/게시글번호       게시글 조회
 *     
 * 프로필    
 * [GET]    /styles/user/@아이디     유저 프로필
 * 
 * 
 * 
 */

@Slf4j
@Controller
@RequestMapping("/styles")
public class PostController {

    @Autowired
    private UserService userService;
    
    /**
     * 전체 게시글 목록
     * @return
     */
    @GetMapping("")
    public String list() {
        return "/post/list";
    }

    /**
     * 게시글 상세
     * @return
     */
    @GetMapping("/read")
    public String read() {
        return "/post/read";
    }

    /**
     * 게시글 상세 (무플버전 확인용)
     * @return
     */
    @GetMapping("/cmmtX")
    public String cmmtTest() {
        return "/post/read_cmmtX";
    }

    /**
     * 게시글 등록 페이지
     * @return
     */
    @GetMapping("/insert")
    public String moveToInsert() {
        return "/post/insert";
    }

    /**
     * 게시글 등록 처리
     * @param userId
     * @return
     */
    // @PostMapping("")
    // public String insert(Style style) {
        
    //     return entity;
    // }

    @GetMapping("/update")
    public String moveToUpdate() {
        return "/post/update";
    }
    
    
    /*
     * 내 스타일 (유저 프로필)
     */
    @GetMapping("/user/{userId}")
    public String usersStyle(@PathVariable("userId") String userId, Model model) throws Exception {
        
        log.info("프로필 이동중...");
        log.info("유저 아이디: " + userId);

        Users user = userService.select(userId);
        log.info(user.toString());
        

        model.addAttribute("user", user);

        return "/post/user/profile";
    }
    
    
}
