package com.springproject.goodz.post.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeMapper {
    
    // 좋아요 여부 체크 - id 기준
    public int listById(String userId, int postNo) throws Exception;

    // 댓글 갯수 가져오기
    public int countLike(int postNo) throws Exception;

}
