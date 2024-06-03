package com.springproject.goodz.post.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springproject.goodz.post.dto.Post;
import com.springproject.goodz.post.service.CommentService;
import com.springproject.goodz.post.service.PostService;
import com.springproject.goodz.user.dto.Users;
import com.springproject.goodz.user.service.UserService;
import com.springproject.goodz.utils.dto.Files;
import com.springproject.goodz.utils.service.FileService;

import lombok.extern.slf4j.Slf4j;

/*
 * 스타일 게시글
 * [GET]    /styles                  게시글 목록
 * [GET]    /styles/게시글번호       게시글 조회
 * [GET]    /styles/update           게시글 수정페이지
 * [GET]    /styles/insert           게시글 수정페이지
 * [POST]   /styles/insert           게시글 작성처리
 * [DELETE] /styles/게시글번호       게시글 조회
 *     
 * 프로필    
 * [GET]    /styles/user/@닉네임     유저 프로필
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

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Autowired
    private CommentService cmmtService;
    
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
     * @throws Exception 
     */
    @GetMapping("/{postNo}")
    public String read(@PathVariable("postNo")int postNo, Model model, HttpSession session) throws Exception {

        /* 게시글 조회 */
        Post post = postService.select(postNo);
        model.addAttribute("post", post);

        /* 첨부파일 조회 */
        Files file = new Files();
        file.setParentTable("post");
        file.setParentNo(post.getPostNo());
        log.info("조회할 파일의 parentTable: " + file.getParentTable());
        log.info("조회할 파일의 parentNo: " + file.getParentNo());
        List<Files> fileList = fileService.listByParent(file);
        model.addAttribute("fileList", fileList);

        /* 게시글 작성자 정보 세팅 */
        Users writer = userService.select(post.getUserId());
        log.info("작성자: " + writer.toString()); 
        model.addAttribute("writer", writer);

        /* 세션정보 세팅 */
        Users loginUser = (Users)session.getAttribute("user");
        model.addAttribute("loginUser", loginUser);

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
    public String moveToInsert(Model model,HttpSession session) {

        // 로그인된 user의 정보를 가져옴
        Users loginUser= (Users)session.getAttribute("user");

        model.addAttribute("loginUser", loginUser);
        log.info("작성화면 이동...");

        return "/post/insert";
    }

    /**
     * 게시글 등록 처리
     * @param userId
     * @return
     * @throws Exception 
     */
    @PostMapping("/insert")
    public String insert(Post post, Model model, HttpSession session) throws Exception {

        log.info(post.toString());

        /* ⬇️ 게시글 등록 처리⬇️ */
        int result = postService.insert(post); // 성공 -> 1

        if (result == 0) {
            log.info("게시글 등록 처리 시, 예외발생");

            return "/post/insert";
        }

        /* ⬇️프로필로 리다이렉트 처리⬇️ */

        // 로그인된 user의 정보를 가져옴
        Users loginUser= (Users)session.getAttribute("user"); 

        // 프로필로 리다이렉트를 위해 닉네임 필요하므로 아이디로 회원 조회
        Users requested = userService.select(post.getUserId());
        log.info(requested.getNickname() + "의 프로필로 이동중...");

        List<Post> postList = postService.selectById(requested.getUserId());

        model.addAttribute("postList", postList);
        model.addAttribute("requested", requested);
        model.addAttribute("loginUser", loginUser);

        return "redirect:/styles/user/@"+requested.getNickname();
    }

    @GetMapping("/update")
    public String moveToUpdate() {
        return "/post/update";
    }
    
    
    /*
     * 유저 프로필
     */
    @GetMapping("/user/@{nickname}")
    public String usersStyle(@PathVariable("nickname") String nickname, Model model, HttpSession session) throws Exception {
        log.info("::::::::::postController::::::::::");
        log.info(nickname + "의 프로필로 이동중...");
        
        Users requested = userService.selectByNickname(nickname);
        
        // 로그인된 user의 정보를 가져옴
        Users loginUser= (Users)session.getAttribute("user");    

        if (loginUser == null) {
            log.info("로그인이 되지않은 사용자");
        }

        /* 게시글 조회 */
        List<Post> postList = postService.selectById(requested.getUserId());

        model.addAttribute("requested", requested);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("postList", postList);

        return "/post/user/profile";
    }
    
    
}
