package com.springproject.goodz.post.service;

import com.springproject.goodz.post.dto.Tag;

public interface TagService {
    
    // 상품태그 추가 - 게시글 등록 시
    public int insert(Tag tag) throws Exception;
    
}
