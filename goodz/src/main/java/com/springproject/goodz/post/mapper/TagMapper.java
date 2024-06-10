package com.springproject.goodz.post.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.springproject.goodz.post.dto.Tag;

@Mapper
public interface TagMapper {
    
    // 상품태그 리스트 조회 - postNo기준(종속된 게시글 기준)
    public List<Tag> taggedList(int postNo) throws Exception;

    // 상품태그 추가 - 게시글 등록 시
    public int insert(Tag tag) throws Exception;

    // 게시글에 종속된 상품태그 삭제 - 게시글 수정 시
    public int delete(int postNo) throws Exception;


}
