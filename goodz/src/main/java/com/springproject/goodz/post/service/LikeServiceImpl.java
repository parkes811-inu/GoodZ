package com.springproject.goodz.post.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.springproject.goodz.post.mapper.LikeMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LikeServiceImpl implements LikeService{

    
    @Autowired
    private LikeMapper likeMapper;

    /**
     * 좋아요 여부 조회 - id 기준
     */
    @Override
    public boolean listById(String userId, int postNo) throws Exception {
        int result = likeMapper.listById(userId, postNo);
        boolean ischecked = false;

        if (result == 0) {
            return ischecked;
        }

        return !ischecked;
    }

    @Override
    public int countLike(int postNo) throws Exception {
        int result = likeMapper.countLike(postNo);

        return result;

    }
    
}
