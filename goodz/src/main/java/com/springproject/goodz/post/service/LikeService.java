package com.springproject.goodz.post.service;

public interface LikeService{

    // 좋아요 여부 조회 - id 기준
    public boolean listById(String userId, int postNo) throws Exception;

    // 댓글 갯수 가져오기
    public int countLike(int postNo) throws Exception;
    
}
