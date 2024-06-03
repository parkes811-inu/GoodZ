package com.springproject.goodz.post.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springproject.goodz.post.service.LikeService;
import com.springproject.goodz.user.dto.Users;

import lombok.extern.slf4j.Slf4j;


/**
 * 좋아요
 * [GET]    /like                   체크 유/무
 * [POST]   /like                   체크 등록 요청
 * [DELETE] /like                   체크 해제 요청
 * [GET]    /like/count/게시글번호   게시글에 체크된 갯수
 */

@Slf4j
@Controller
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;
    
    // @GetMapping("")
    // public String getMethodName(@RequestParam String param) {
    //     return new String();
    // }

    /**
     * 게시글에 체크된 좋아요 갯수 조회
     * @param postNo
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping("/count/{postNo}")
    public String getMethodName(@PathVariable("postNo") int postNo, HttpSession session) throws Exception {

        int countLike = likeService.countLike(postNo);
        

        return new String();
    }
    // cmmtController 참고해서 숫자 Map으로 반환하고 숫자 띄우는거 할 차례임
    
    
}
