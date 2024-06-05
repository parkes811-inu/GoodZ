package com.springproject.goodz.post.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springproject.goodz.post.dto.Like;
import com.springproject.goodz.post.dto.Post;
import com.springproject.goodz.post.mapper.LikeMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LikeServiceImpl implements LikeService{

    
    @Autowired
    private LikeMapper likeMapper;

    /**
     * 좋아요 여부 조회 - id 기준
     */
    @Override
    public boolean listById(Like like) throws Exception {
        int result = likeMapper.listById(like);
        boolean ischecked = false;  // 체크여부 off -> false / on -> true

        if (result == 0) {
            return ischecked;
        }

        return !ischecked;
    }
    
    // 좋아요 갯수 갱신
    @Override
    public int countLike(int postNo) throws Exception {
        int result = likeMapper.countLike(postNo);

        return result;
    }

    // 좋아요 off -> on
    @Override
    public int likeOn (Like like) throws Exception {
        int result = likeMapper.likeOn(like);

        return result;
    }

    // 좋아요 on -> off
    @Override
    public int likeOff (Like like) throws Exception {
        int result = likeMapper.likeOff(like);

        return result;
    }
    
}
