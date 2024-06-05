package com.springproject.goodz.user.service;

import java.util.List;

import com.springproject.goodz.user.dto.Follow;
import com.springproject.goodz.user.dto.Users;

public interface FollowService {

    // 팔로워 여부 조회
    public boolean isFollower(Follow follow) throws Exception;

    // 팔로워 조회
    public List<Follow> followerList(String userId) throws Exception;

    // 팔로잉 조회
    public List<Follow> followingList(String followerId) throws Exception;

    // 팔로우 요청
    public int addFollow(Follow follow) throws Exception;
    
    // 언팔 요청
    public int unfollow(int no) throws Exception;
    
}
