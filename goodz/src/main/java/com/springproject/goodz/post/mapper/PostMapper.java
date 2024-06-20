package com.springproject.goodz.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springproject.goodz.post.dto.Post;

@Mapper
public interface PostMapper {

    
    // 게시글 목록
    public List<Post> list() throws Exception;

    // 게시글 조회
    public Post select(int postNo) throws Exception;

    // 게시글 조회 - id 기준
    public List<Post> selectById(String userId) throws Exception;

    // 마지막 게시글번호 조회 - 첨부파일 등록 시 필요함
    public int maxNo() throws Exception;

    // 게시글 등록
    public int insert(Post post) throws Exception;

    // 게시글 수정
    public int update(Post post) throws Exception;

    // 게시글 삭제
    public int delete(int postNo) throws Exception;

    // 인기게시글 4개 - 메인화면 조회용
    public List<Post> popularPosts(@Param("offset") int offset, @Param("size") int sizee) throws Exception;

    // 게시글 조회 (대표이미지파일 번호 포함) - 게시글 번호 기준
    public List<Post> taggedPost(int productNo) throws Exception;
    
}
