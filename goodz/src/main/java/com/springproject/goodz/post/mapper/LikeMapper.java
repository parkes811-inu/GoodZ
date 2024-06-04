package com.springproject.goodz.post.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.springproject.goodz.post.dto.Like;
import com.springproject.goodz.post.dto.Post;

@Mapper
public interface LikeMapper {

    // 좋아요 여부 체크 - id 기준
    public int listById(Post post) throws Exception;

    // 좋아요 갯수 가져오기
    public int countLike(int postNo) throws Exception;

    // 좋아요 off -> on
    public int likeOn (Like like) throws Exception;

}
