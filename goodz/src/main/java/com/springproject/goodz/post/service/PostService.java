package com.springproject.goodz.post.service;

import java.util.List;

import com.springproject.goodz.post.dto.Post;

public interface PostService {

    // 게시글 목록
    public List<Post> list() throws Exception;

    // 게시글 조회
    public Post select(int no) throws Exception;

    // 게시글 조회 - id 기준
    public List<Post> selectById() throws Exception;

    // 게시글 등록
    public int insert(Post style) throws Exception;

    // 게시글 수정
    public int update(Post style) throws Exception;

    // 게시글 삭제
    public int delete(int postNo) throws Exception;
}
