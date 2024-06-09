package com.springproject.goodz.user.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Users {
    private int no;                     // 번호 -> 프로필 사진 parentNo.
    private String userId;
    private String username;
    private String nickname;
    private String password;
    private String birth;
    private String phoneNumber;
    private String profilePictureUrl;
    private int profileImgNo;           // 프로필 이미지 번호
    private MultipartFile profileImgFile; 
    private String account;
    private Date createdAt;
    private Date updatedAt;
    private int enabled;
    private List<UserAuth> authList;
    private List<Users> followList;     // 팔로워/팔로잉 목록

    private int countFollower;          // 팔로워 수
    private int countFollowing;         // 팔로잉 수

}