package com.springproject.goodz.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.springproject.goodz.user.dto.Follow;
import com.springproject.goodz.user.service.FollowService;

import lombok.extern.slf4j.Slf4j;



/**
 * [GET]        /follow/userId  팔로워/팔로잉 갱신
 * [POST]       /follow         팔로우 신청
 * [DELETE]     /follow/번호    팔로우 신청
 */

@Slf4j
@Controller
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @GetMapping("/{userId}")
    public String followCount(@PathVariable("userId")String userId) {

        try {
            List<Follow> followerList = followService.followerList(userId);
            List<Follow> followingList = followService.followingList(userId);

        } catch (Exception e) {
            log.info("팔로워 조회 시 예외발생");
            e.printStackTrace();
        }


        return new String();
    }
    


    

}
