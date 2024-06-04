package com.springproject.goodz.post.service;

import com.springproject.goodz.post.dto.Like;

public interface LikeService{

    // 좋아요 여부 조회 - id 기준
    public boolean listById(String userId, int postNo) throws Exception;

    // 좋아요 off -> on
    public int likeOn (Like like) throws Exception;

    // 좋아요 갯수 가져오기
    public int countLike(int postNo) throws Exception;
    
}
