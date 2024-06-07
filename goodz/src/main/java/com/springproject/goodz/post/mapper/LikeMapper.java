package com.springproject.goodz.post.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.springproject.goodz.post.dto.Like;

@Mapper
public interface LikeMapper {

    // 좋아요 체크 여부 - id 기준
    public int listById(Like like) throws Exception;

    // 좋아요 갯수 가져오기
    public int countLike(int postNo) throws Exception;

    // 좋아요 off -> on
    public int likeOn (Like like) throws Exception;

    // 좋아요 on -> off
    public int likeOff (Like like) throws Exception;

}
