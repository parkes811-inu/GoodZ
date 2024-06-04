package com.springproject.goodz.post.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springproject.goodz.post.dto.Like;
import com.springproject.goodz.post.service.LikeService;
import com.springproject.goodz.user.dto.Users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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
     * 좋아요 등록 요청 (off -> on)
     * @param like
     * @return
     * @throws Exception
     */
    @PostMapping("")
    public ResponseEntity<String> likeOn(@RequestBody Like like) throws Exception {
        log.info("좋아요 on 요청");
        log.info("요청게시글: " + like.getPostNo());

        int result = likeService.likeOn(like);

         if (result == 0) {
            // 데이터 처리 실패
            return new ResponseEntity<>("FAIL", HttpStatus.OK); // OK = 200
        }
        
        //데이터 처리 성공
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<String> likeOff(@RequestBody Like like) throws Exception {
        log.info("좋아요 off 요청");
        log.info("요청게시글: " + like.getPostNo());

        int result = likeService.likeOff(like);

         if (result == 0) {
            // 데이터 처리 실패
            return new ResponseEntity<>("FAIL", HttpStatus.OK); // OK = 200
        }
        
        //데이터 처리 성공
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    } 
    

    /**
     * 게시글에 체크된 좋아요 갯수 조회
     * @param postNo
     * @param session
     * @return
     * @throws Exception
     */
    @GetMapping("/count/{postNo}")
    public ResponseEntity<Map<String, Long>> countLike(@PathVariable("postNo") int postNo, HttpSession session) throws Exception {
        
        long countLike = likeService.countLike(postNo);

        log.info("업데이트된 좋아요 수: " + countLike);

        Map<String, Long> response = new HashMap<>();
        response.put("countLike", countLike );
        

        return ResponseEntity.ok(response);
    }
    
    
    
}
