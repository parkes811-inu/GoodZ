package com.springproject.goodz.user.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Users {
    private String userId;
    private String username;
    private String nickname;
    private String password;
    private String birth;
    private String phoneNumber;
    private String profilePictureUrl;
    private String account;
    private Date createdAt;
    private Date updatedAt;
    private int enabled;
    private List<UserAuth> authList;
}