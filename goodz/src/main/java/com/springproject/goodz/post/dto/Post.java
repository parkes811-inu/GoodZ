package com.springproject.goodz.post.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Post {
    
    private int postNo;                 // 게시글 번호
    private String userId;              // 작성자 아이디
    private String nickname;            // 작성자 닉네임 - dto 조회용
    private int profileImgNo;          // 작성자 프로필이미지번호 - dto 조회용
    private String content;             // 작성내용

    private int likeCount;              // 좋아요 갯수
    private int wishCount;              // 저장 갯수
    private String isLiked;             // 좋아요 체크 여부 -> 'fill' / 'none'
    private String isWishlisted;        // 저장 체크 여부   -> 'fill' / 'none'
    
    private Date createdAt;             // 작성일자
    private Date updatedAt;             // 수정일자

    
    // 파일에 대한 변수를 받기위해 정보 추가
    List<MultipartFile> attachedFiles;

    // 대표 이미지의 인덱스 번호
    private int mainImgIndex;

    // 대표이미지 파일번호 (file_code = 1 인 파일)
    private int fileNo;

}
