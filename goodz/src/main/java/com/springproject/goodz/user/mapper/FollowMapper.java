package com.springproject.goodz.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.springproject.goodz.user.dto.Follow;
import com.springproject.goodz.user.dto.Users;

@Mapper
public interface FollowMapper {
    
    // 팔로워 여부 조회
    public int isFollower(Follow follow) throws Exception;

    // 팔로워 조회
    public List<Follow> followerList(String userId) throws Exception;

    // 팔로잉 조회
    public List<Follow> followingList(String followerId) throws Exception;

    // 팔로우 요청
    public int addFollow(Follow follow) throws Exception;
    
    // 언팔 요청
    public int unfollow(Follow follow) throws Exception;

    // 팔로워/팔로잉 수 조회
    public Users countFollow(String UserId) throws Exception;
}
