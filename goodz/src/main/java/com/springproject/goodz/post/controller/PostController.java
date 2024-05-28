package com.springproject.goodz.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/*
 * 스타일 게시글
 * [GET]    /styles                  게시글 목록
 * [GET]    /styles/게시글번호       게시글 조회
 * [GET]    /styles/update           게시글 수정페이지
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

    /**
     * 게시글 등록 페이지
     * @return
     */
    @GetMapping("/insert")
    public String moveToInsert() {
        return "/style/insert";
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
        return "/style/update";
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
