package com.springproject.goodz.user.controller;

import java.util.ArrayList;
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

import com.springproject.goodz.user.dto.Follow;
import com.springproject.goodz.user.dto.Users;
import com.springproject.goodz.user.service.FollowService;
import com.springproject.goodz.user.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;





/**
 * [GET]        /follow/userId  팔로워/팔로잉 갱신
 * [POST]       /follow         팔로우 신청
 * [DELETE]     /follow         언팔 신청
 */

@Slf4j
@Controller
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;


    /**
     * 팔로워 조회 - 프로필계정 요청한 id 기준
     */
    @GetMapping("/follower/{userId}")
    public String followerCount(@PathVariable("userId")String profileId, Model model, HttpSession session) {

         try {
             
            // ⭐ 프로필 계정 세팅
            Users profileUser = userService.select(profileId);

            
            // 팔로워 목록과 수 조회
            Map<String, Object> followerDetails = followService.getFollowerDetails(profileId);
            List<Users> followerList = (List<Users>) followerDetails.get("followerList");
            int count = (int) followerDetails.get("followerCount");
            
            profileUser.setFollowList(followerList);
            
            log.info(profileUser.getNickname() + "님의 팔로워 수: " + count);
            
            model.addAttribute("profileUser", profileUser);

            // 👤 세션계정 세팅 및 팔로잉 목록 가져오기
            Users loginUser = (Users)session.getAttribute("user");
            // 비로그인 상태면 리턴.
            if (loginUser == null) {
                return "/post/user/follow";
            }
            Map<String, Object> followingDetails = followService.getFollowingDetails(loginUser.getUserId());
            List<Users> LoginUserFollowingList = (List<Users>) followingDetails.get("followingList");

            model.addAttribute("loginUser", loginUser);
            model.addAttribute("LoginUserFollowingList", LoginUserFollowingList);
            
        } catch (Exception e) {
            log.info("팔로워 조회 시 예외 발생");
            e.printStackTrace();
        }

        return "/post/user/follow";
    }       

    /**
     * 팔로잉 조회 - 프로필계정 id 기준
     * @param userId
     * @param model
     * @return
     */
    @GetMapping("/following/{userId}")
    public String followingCount(@PathVariable("userId")String profileId, Model model, HttpSession session) {

        try {

            // 프로필 계정 세팅
            Users profileUser = userService.select(profileId);

            // 팔로워 목록과 수 조회
            Map<String, Object> followingDetails = followService.getFollowingDetails(profileId);
            List<Users> followingList = (List<Users>) followingDetails.get("followingList");
            int count = (int) followingDetails.get("followingCount");

            profileUser.setFollowList(followingList);

            log.info(profileUser.getNickname() + "님의 팔로워 수: " + count);

            model.addAttribute("profileUser", profileUser);

            // 👤 세션계정 세팅 및 팔로잉 목록 가져오기
            Users loginUser = (Users)session.getAttribute("user");
            // 비로그인 상태면 리턴.
            if (loginUser == null) {
                return "/post/user/follow";
            }
            followingDetails = followService.getFollowingDetails(loginUser.getUserId());
            List<Users> loginUserFollowingList = (List<Users>) followingDetails.get("followingList");

            model.addAttribute("loginUser", loginUser);
            model.addAttribute("loginUserFollowingList", loginUserFollowingList);

        } catch (Exception e) {
            log.info("팔로잉 조회 시 예외 발생");
            e.printStackTrace();
        }

        return "/post/user/follow";
    }

    /**
     * 팔로우 등록처리
     * @param follow
     * @return
     * @throws Exception
     */
    @PostMapping("/follow")
    public ResponseEntity<String> addFollow(@RequestBody Follow follow) throws Exception {
        log.info("팔로우 요청");
        log.info(follow.getFollowerId() + " -> " + follow.getUserId());

        int result = followService.addFollow(follow);

        if (result == 0) {
            //팔로우 등록 실패
            return new ResponseEntity<>("FAIL", HttpStatus.OK);
        }

        // 팔로우 등록 성공
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    /**
     * 언팔 처리 (=팔로우 삭제 처리)
     * @param follow
     * @return
     * @throws Exception
     */
    @DeleteMapping("/follow")
    public ResponseEntity<String> unFollow(@RequestBody Follow follow) throws Exception {
        log.info("언팔 요청");
        log.info(follow.getFollowerId() + " -/-> " + follow.getUserId());


        int result = followService.unFollow(follow);

        if (result == 0) {
            //팔로우 등록 실패
            return new ResponseEntity<>("FAIL", HttpStatus.OK);
        }

        // 팔로우 등록 성공
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    @GetMapping("/follow/count/{profileId}")
    public ResponseEntity<Map<String, Integer>> countFollow(@PathVariable("profileId") String profileId) throws Exception {


        Map<String, Integer> countFollow =  followService.countFollow(profileId);

        log.info("업데이트된 팔로워 수: " + countFollow.get("countFollower"));
        log.info("업데이트된 팔로잉 수: " + countFollow.get("countFollowing"));

        return ResponseEntity.ok(countFollow);
    }
    
    
}
