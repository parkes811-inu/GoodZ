package com.springproject.goodz.post.service;

import com.springproject.goodz.post.dto.Tag;

public interface TagService {
    
    // 상품태그 추가 - 게시글 등록 시
    public int insert(Tag tag) throws Exception;

    // 게시글에 종속된 상품태그 삭제 - 게시글 수정 시
    public int delete(int postNo) throws Exception;
    
}
