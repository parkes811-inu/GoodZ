package com.springproject.goodz.post.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springproject.goodz.post.dto.Comment;
import com.springproject.goodz.post.service.CommentService;
import com.springproject.goodz.user.dto.Users;
import com.springproject.goodz.user.service.UserService;

import lombok.extern.slf4j.Slf4j;


/*
 * 댓글
 * [GET]        /cmmt/게시글번호        종속된 전체 댓글 목록
 * [POST]       /cmmt                  댓글 등록 요청    
 * [DELETE]     /cmmt/댓글번호          댓글 삭제 요청
 * [GET]        /cmmt/count/게시글번호  종속된 댓글 갯수 조회    
 * 
 * 프로필    
 * [GET]    /styles/user/@닉네임     유저 프로필
 * 
 */
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
    public String list(@PathVariable("postNo") int postNo, Model model, HttpSession session) throws Exception {

        List<Comment> cmmtList = cmmtservice.list(postNo);

        for (Comment cmmt : cmmtList) {

            Users user = userService.select(cmmt.getUserId());
            cmmt.setNickname(user.getNickname());
        }

        // 
        // model.addAttribute("loginUser", loginUser);
        /* 세션정보 세팅 */
        Users loginUser = (Users)session.getAttribute("user");
        model.addAttribute("loginUser", loginUser);
        
        model.addAttribute("cmmtList", cmmtList);
        return "/post/cmmt/list";
    }

    /**
     * 댓글 등록
     * @param cmmt
     * @return
     * @throws Exception
     */
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

    /**
     * 댓글 삭제 요청
     * @param cNo
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{no}")
    public ResponseEntity<String> delete(@PathVariable("no") int cNo) throws Exception{
        log.info("삭제할 댓글 번호: " + cNo);
        int result = cmmtservice.delete(cNo);

        if (result == 0) {
            // 삭제 처리 실패
            return new ResponseEntity<>("FAIL", HttpStatus.OK); // OK = 200
        }

        // 삭제 처리 성공
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    /**
     * 종속된 댓글 갯수 조회
     * @param postNo
     * @return
     * @throws Exception
     */
    @GetMapping("/count/{postNo}")
    public ResponseEntity<Map<String, Long>> count(@PathVariable("postNo")int postNo) throws Exception {

        long cmmtQty = cmmtservice.cmmtQty(postNo);
        
        Map<String, Long> response = new HashMap<>();
        response.put("cmmtQty", cmmtQty);

        return ResponseEntity.ok(response);
    }
    
    
    
}
