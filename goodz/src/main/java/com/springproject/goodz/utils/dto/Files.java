package com.springproject.goodz.utils.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Files {
    
    private int no;                 // 파일 번호
    private String parentTable;     // 속한 테이블 ("product인지 post인지")
    private int parentNo;           // 속한 번호 (product이면 상품번호, post면 게시글번호)
    private String fileName;        // 변경된 파일명 ( UID_원본파일명.확장자 )
    private String originName;      // 원본 파일명
    private String filePath;        // 저장된 파일 경로
    private long fileSize;          // 파일 사이즈
    private Date regDate;           // 등록일자
    private Date updDate;           // 수정일자
    private int fileCode;           // 대표 이미지 여부: 0-일반 1-대표
    
    private MultipartFile file;     // 첨부한 이미지를 담을 변수
    
}
