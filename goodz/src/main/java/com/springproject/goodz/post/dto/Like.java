package com.springproject.goodz.post.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Like {
    private int likeNo;         // 좋아요 번호
    private int cNo;            // 댓글 번호
    private String userId;      // 유저 아이디
    private int postNo;         // 게시글 번호
    private Date createdAt;     // 좋아요 누른 날
    private Date updatedAt;     // 좋아요 취소 날
}
