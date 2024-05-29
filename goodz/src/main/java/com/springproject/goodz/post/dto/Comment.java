package com.springproject.goodz.post.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Comment {
    private int cNo;            // 댓글 번호
    private int postNo;         // 게시글 번호
    private String userId;      // 유저 아이디
    private String comment;     // 댓글 내용
    private Date createdAt;     // 댓글 입력 날짜
    private Date updatedAt;     // 댓글 수정 날짜

}
