package com.springproject.goodz.post.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springproject.goodz.post.dto.Comment;
import com.springproject.goodz.post.service.CommentService;
import com.springproject.goodz.user.dto.Users;
import com.springproject.goodz.user.service.UserService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Slf4j
@Controller
@RequestMapping("/cmmt")
public class CommentController {
    
    @Autowired
    private CommentService cmmtservice;

    @Autowired
    private UserService userService;

    /**
     * 종속된 댓글 목록 조회 - SSR 방식(Server Side Rendering; 서버측에서 렌더링 후 HTML뷰 응답)
     * @param postNo
     * @return
     * @throws Exception
     */
    @GetMapping("/{postNo}")
    public String list(@PathVariable("postNo") int postNo, Model model) throws Exception {

        List<Comment> cmmtList = cmmtservice.list(postNo);

        for (Comment cmmt : cmmtList) {

            Users user = userService.select(cmmt.getUserId());
            cmmt.setNickname(user.getNickname());
        }

        model.addAttribute("cmmtList", cmmtList);

        return "/post/cmmt/list";
    }

    @PostMapping("")
    public ResponseEntity<String> insert(@RequestBody Comment cmmt) throws Exception {
        log.info("::::::::댓글등록요청::::::::");
        log.info("댓글: " + cmmt);

        // 댓글 등록 요청
        int result = cmmtservice.insert(cmmt);

        if (result == 0) {
            // 데이터 처리 실패
            return new ResponseEntity<>("FAIL", HttpStatus.OK); // OK = 200
        }
        
        
        //데이터 처리 성공
        return new ResponseEntity<>("SUCCESS", HttpStatus.CREATED); // CREATED = 201
    }
    
    
}
