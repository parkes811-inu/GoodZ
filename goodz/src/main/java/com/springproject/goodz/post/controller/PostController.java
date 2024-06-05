package com.springproject.goodz.post.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;

import com.springproject.goodz.post.dto.Like;
import com.springproject.goodz.post.dto.Post;
import com.springproject.goodz.post.service.LikeService;
import com.springproject.goodz.post.service.PostService;
import com.springproject.goodz.user.dto.Users;
import com.springproject.goodz.user.dto.Wish;
import com.springproject.goodz.user.service.UserService;
import com.springproject.goodz.user.service.WishListService;
import com.springproject.goodz.utils.dto.Files;
import com.springproject.goodz.utils.service.FileService;

import lombok.extern.slf4j.Slf4j;



/*
 * 스타일 게시글
 * [GET]    /styles                     전체 게시글 목록
 * [GET]    /styles/게시글번호           게시글 조회
 * [GET]    /styles/update/게시글번호    게시글 수정페이지
 * [POST]   /styles/update              게시글 수정처리
 * [GET]    /styles/insert              게시글 수정페이지
 * [POST]   /styles/insert              게시글 작성처리
 * [POST]   /styles/delete/게시글번호    게시글 삭제
 *     
 * 프로필    
 * [GET]    /styles/user/@닉네임     유저 프로필
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
    private LikeService likeService;

    @Autowired
    private WishListService wishListService;
    
    /**
     * 전체 게시글 목록
     * @return
     */
    @GetMapping("")
    public String list(Model model, HttpSession session) throws Exception {

        // 게시글 세팅
        List<Post> postList = postService.list();
        
        // 세션 정보 세팅
        Users loginUser = (Users)session.getAttribute("user");
        model.addAttribute("loginUser", loginUser);
        
        /* 좋아요 & 저장 세팅 */
        // 비 로그인 시, 좋아요 전체 해제
        if (loginUser == null) {
            for (Post post : postList) {
                post.setIsLiked("none");
                post.setIsWishlisted("none");
            }
            model.addAttribute("postList", postList);
            
            return "/post/list";

        } else {
            for (Post post : postList) {
                // 세션아이디와 게시글 번호 기준으로 좋아요 여부 확인
                Like like = new Like();
                like.setUserId(loginUser.getUserId());
                like.setPostNo(post.getPostNo());
                boolean isChecked_like = likeService.listById(like);
                
                if (!isChecked_like) {
                    post.setIsLiked("none");
                } else {
                    post.setIsLiked("solid");
                }

                // 세션아이디와 게시글 번호 기준으로 저장 여부 확인
                Wish wish = new Wish();
                wish.setUserId(loginUser.getUserId());
                wish.setParentTable("post");
                wish.setParentNo(post.getPostNo());
                boolean isChecked_wishlist = wishListService.listById(wish);

                if (!isChecked_wishlist) {
                    post.setIsWishlisted("none");
                } else {
                    post.setIsWishlisted("solid");
                }
            }
            model.addAttribute("postList", postList);
        }

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
        log.info("저장 갯수: "+post.getWishCount() + "개");
        
        /* 첨부파일 조회 */
        Files file = new Files();
        file.setParentTable("post");
        file.setParentNo(post.getPostNo());
        List<Files> fileList = fileService.listByParent(file);
        model.addAttribute("fileList", fileList);
        
        /* 게시글 작성자 정보 세팅 */
        Users writer = userService.select(post.getUserId());
        log.info("작성자: " + writer.toString()); 
        model.addAttribute("writer", writer);
        
        /* 세션정보 세팅 */
        Users loginUser = (Users)session.getAttribute("user");
        model.addAttribute("loginUser", loginUser);
        
        /* 좋아요 & 저장 세팅 */
        if (loginUser == null) {
            // 비 로그인 시, 좋아요 표시 전체 해제
            log.info("로그인이 되지않은 사용자");
            
            post.setIsLiked("none");
            post.setIsWishlisted("none");
            
        } else {
            // 로그인 시, 유저가 체크한 좋아요&저장 표시
            // 세션아이디와 게시글 번호 기준으로 좋아요 여부 확인
            Like like = new Like();
            like.setUserId(loginUser.getUserId());
            like.setPostNo(post.getPostNo());
            boolean isChecked_like = likeService.listById(like);

            if (!isChecked_like) {
                post.setIsLiked("none");
            } else {
                post.setIsLiked("solid");
            }
            
            // 세션아이디와 게시글 번호 기준으로 저장 여부 확인
            Wish wish = new Wish();
            wish.setUserId(loginUser.getUserId());
            wish.setParentTable("post");
            wish.setParentNo(post.getPostNo());
            boolean isChecked_wishlist = wishListService.listById(wish);

            if (!isChecked_wishlist) {
                post.setIsWishlisted("none");
            } else {
                post.setIsWishlisted("solid");
            }
        }
        
        model.addAttribute("post", post);

        return "/post/read";
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

        /* 게시글 조회 */
        List<Post> postList = postService.selectById(requested.getUserId());
        
        // 비 로그인 시, 좋아요 표시 전체 해제
        if (loginUser == null) {
            log.info("로그인이 되지않은 사용자");
            
            for (Post post : postList) {
                post.setIsLiked("none");
                post.setIsWishlisted("none");
            }
            
            // 로그인 시, 유저가 체크한 좋아요 표시
        } else {
            
            for (Post post : postList) {
                // 세션아이디와 게시글 번호 기준으로 좋아요 여부 확인
                Like like = new Like();
                like.setUserId(loginUser.getUserId());
                like.setPostNo(post.getPostNo());
                boolean isChecked_like = likeService.listById(like);
                
                if (!isChecked_like) {
                    post.setIsLiked("none");
                } else {
                    post.setIsLiked("solid");
                }

                // 세션아이디와 게시글 번호 기준으로 저장 여부 확인
                Wish wish = new Wish();
                wish.setUserId(loginUser.getUserId());
                wish.setParentTable("post");
                wish.setParentNo(post.getPostNo());
                boolean isChecked_wishlist = wishListService.listById(wish);

                if (!isChecked_wishlist) {
                    post.setIsWishlisted("none");
                } else {
                    post.setIsWishlisted("solid");
                }
            }
        }
        
        model.addAttribute("requested", requested);
        model.addAttribute("loginUser", loginUser);
        model.addAttribute("postList", postList);

        return "/post/user/profile";
    }

    /**
     * 게시글 수정 페이지
     * @param postNo
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/update/{postNo}")
    public String moveToUpdate(@PathVariable("postNo")int postNo, Model model) throws Exception {

        /* 게시글 조회 */
        Post post = postService.select(postNo);
        model.addAttribute("post", post);

        /* 첨부파일 조회 */
        Files file = new Files();
        file.setParentTable("post");
        file.setParentNo(post.getPostNo());
        List<Files> fileList = fileService.listByParent(file);
        model.addAttribute("fileList", fileList);

        return "/post/update";
    }

    /**
     * 게시글 수정 처리
     * @param post
     * @param model
     * @return
     */
    @PostMapping("/update")
    public String update(Post post, Model model) {

        /* ⬇️ 게시글 수정 처리 ⬇️ */
        int result;

        try {
            result = postService.update(post);
        } catch (Exception e) {
            System.err.println("게시글 수정 처리 시, 예외발생");
            e.printStackTrace();
            return "forward:/styles/update/" + post.getPostNo();
        }

        /* ⬇️ 게시글 조회 페이지로 리다이렉트 처리 ⬇️ */
        log.info(post.getPostNo() + "번 게시글 수정 성공");
        
        return "redirect:/styles/"+post.getPostNo();
    }
    
    /**
     * 게시글 삭제 요청
     * @param postNo
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{postNo}")
    public ResponseEntity<String> delete(@PathVariable("postNo") int postNo) throws Exception{
        log.info("삭제할 게시글 번호: " + postNo);
        Post post = postService.select(postNo);

        /* ⬇️ 게시글 삭제처리 ⬇️ */
        int result = postService.delete(postNo);
        if (result == 0) {
            // 삭제 처리 실패
            return new ResponseEntity<>("FAIL", HttpStatus.OK);
        }

        // 댓글과 좋아요는 postNo가 외래키 ON DELETE CASCADE 조건으로 같이 삭제됨.
        
        /* ⬇️ 첨부파일 삭제처리 ⬇️ */
        Files file = new Files();
        file.setParentTable("post");    // parnetTable
        file.setParentNo(postNo);                   // parentNo
        fileService.deleteByParent(file);

        /* ⬇️ wishlist 테이블 삭제처리 ⬇️ */
        Wish wish = new Wish();
        wish.setParentTable("post");    // parnetTable
        wish.setParentNo(post.getPostNo());         // parentNo
        wishListService.deleteAll(wish);

        // 삭제 처리 성공
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    
}
