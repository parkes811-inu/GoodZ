package com.springproject.goodz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


/*
 * 스타일 게시글
 * [GET]    /styles                 게시글 목록
 * [GET]    /styles/게시글번호       게시글 조회
 * [POST]   /styles                 게시글 작성요청
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
public class StyleController {
    
    /**
     * 전체 게시글 목록
     * @return
     */
    @GetMapping("")
    public String list() {
        return "/style/list";
    }

    /**
     * 게시글 상세
     * @return
     */
    @GetMapping("/read")
    public String read() {
        return "/style/read";
    }

    /**
     * 게시글 상세 (무플버전 확인용)
     * @return
     */
    @GetMapping("/cmmtX")
    public String cmmtTest() {
        return "/style/read_cmmtX";
    }

    @GetMapping("/insert")
    public String insert() {
        return "/style/insert";
    }
    

    /*
     * 내 스타일 (유저 프로필)
     */
    @GetMapping("/user/{userId}")
    public String usersStyle(@PathVariable("userId") String userId) {
        if (userId.equals("NoOne")) {
            return "/style/user/profile_noPosts";
        }
        return "/style/user/profile";
    }
    
    
}
