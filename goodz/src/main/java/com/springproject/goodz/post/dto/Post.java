package com.springproject.goodz.post.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Post {
    
    private int postNo;         // 게시글 번호
    private String userId;      // 작성자
    private String content;     // 작성내용
    private Date createdAt;     // 작성일자
    private Date updatedAt;     // 수정일자

    
    // 파일에 대한 변수를 받기위해 정보 추가
    List<MultipartFile> attachedFiles;

    // 대표 이미지의 인덱스 번호
    private int mainImgIndex;

    // 대표이미지 파일번호
    private int fileNo;

}
