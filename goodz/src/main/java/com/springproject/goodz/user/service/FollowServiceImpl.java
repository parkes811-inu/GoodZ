package com.springproject.goodz.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.goodz.user.dto.Follow;
import com.springproject.goodz.user.dto.Users;
import com.springproject.goodz.user.mapper.FollowMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FollowServiceImpl implements FollowService{
    
    @Autowired
    private FollowMapper followMapper;

    @Autowired
    private UserService userService;

    // 팔로워 여부 조회
    @Override
    public boolean isFollower(Follow follow) throws Exception {

        int result = followMapper.isFollower(follow);
        boolean isFollwer = false;

        if (result == 0 ) {
            
            return isFollwer;
        }

        return !isFollwer;
    }

    // 팔로워 조회
    @Override
    public List<Follow> followerList(String userId) throws Exception {

        List<Follow> followerList = followMapper.followerList(userId);

        return followerList;
    }

    // 팔로워 목록과 수 조회
    @Override
    public Map<String, Object> getFollowerDetails(String userId) throws Exception {
        
        List<Follow> followers = followerList(userId);
        List<Users> followerList = new ArrayList<>();

        if (followers != null) {
            for (Follow follow : followers) {
                Users follower = userService.select(follow.getFollowerId());
                followerList.add(follower);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("followerList", followerList);
        result.put("followerCount", followerList.size());

        return result;
    }

    // 팔로잉 조회
    @Override
    public List<Follow> followingList(String followerId) throws Exception {

        List<Follow> followingList = followMapper.followingList(followerId);

        return followingList;
    }

    // 팔로잉 목록과 수 조회
    @Override
    public Map<String, Object> getFollowingDetails(String userId) throws Exception{
    
        List<Follow> followings = followingList(userId);
        List<Users> followingList = new ArrayList<>();

        if (followingList != null) {
            for (Follow follow : followings) {
                Users following = userService.select(follow.getUserId());
                followingList.add(following);
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("followingList", followingList);
        result.put("followingCount", followingList.size());

        return result;
    }

    // 팔로우 요청
    @Override
    public int addFollow(Follow follow) throws Exception {
        int result = followMapper.addFollow(follow);

        if (result == 0) {
            log.info("팔로우 요청 실패");
        }

        return result;
    }

    // 언팔 요청
    @Override
    public int unFollow(Follow follow) throws Exception {

        int result = followMapper.unfollow(follow);

        if (result == 0) {
            log.info("언팔 요청 실패");
        }

        return result;
    }

    // 팔로워/팔로잉 수 조회
    @Override
    public Map<String, Integer> countFollow(String profileId) throws Exception {
        Users user = userService.select(profileId);
        
        log.info("조회할 유저: " + user.getUserId());
        log.info("팔로워 수: " + user.getCountFollower());
        log.info("팔로잉 수: " + user.getCountFollowing());

        Map<String, Integer> result = new HashMap<>();
        result.put("countFollower", user.getCountFollower());
        result.put("countFollowing", user.getCountFollowing());

        return result;
    }


}
