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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.springproject.goodz.user.dto.Follow;
import com.springproject.goodz.user.dto.Users;
import com.springproject.goodz.user.service.FollowService;
import com.springproject.goodz.user.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




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
     * 팔로워 조회 - 요청한 id 기준
     */
    @GetMapping("/follower/{userId}")
    public String followerCount(@PathVariable("userId")String userId, Model model) {

         try {
            // 프로필 계정 세팅
            Users profileUser = userService.select(userId);

            // 팔로워 목록과 수 조회
            Map<String, Object> followerDetails = followService.getFollowerDetails(userId);
            List<Users> followerList = (List<Users>) followerDetails.get("followerList");
            int count = (int) followerDetails.get("followerCount");

            profileUser.setFollowList(followerList);
            profileUser.setFollowerCount(count);

            log.info(profileUser.getNickname() + "님의 팔로워 수: " + count);

            model.addAttribute("profileUser", profileUser);

        } catch (Exception e) {
            log.info("팔로워 조회 시 예외 발생");
            e.printStackTrace();
        }

        return "/post/user/follow";
    }       

    @GetMapping("/following/{userId}")
    public String followingCount(@PathVariable("userId")String userId, Model model) {

        try {
            // 프로필 계정 세팅
            Users profileUser = userService.select(userId);

            // 팔로워 목록과 수 조회
            Map<String, Object> followingDetails = followService.getFollowingDetails(userId);
            List<Users> followingList = (List<Users>) followingDetails.get("followingList");
            int count = (int) followingDetails.get("followingCount");

            profileUser.setFollowList(followingList);
            profileUser.setFollowingCount(count);

            log.info(profileUser.getNickname() + "님의 팔로워 수: " + count);

            model.addAttribute("profileUser", profileUser);

        } catch (Exception e) {
            log.info("팔로잉 조회 시 예외 발생");
            e.printStackTrace();
        }

        return "/post/user/follow";
    }

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
    
}
