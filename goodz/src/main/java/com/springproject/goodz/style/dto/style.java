package com.springproject.goodz.style.dto;

import java.util.Date;

import lombok.Data;

@Data
public class style {
    
    private int no;             // 게시글 번호
    private String userId;      // 작성자
    private String content;     // 작성내용
    private String imageUrl;   // 첨부이미지 url
    private Date createdAt;     // 작성일자
    private Date updatedAt;     // 수정일자

}
