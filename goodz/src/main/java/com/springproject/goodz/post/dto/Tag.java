package com.springproject.goodz.post.dto;

import lombok.Data;

@Data
public class Tag {
    private int tNo;        // 상품 태그 번호
    private int pNo;        // 상품 번호
    private int postNo;     // 게시글 번호
}
