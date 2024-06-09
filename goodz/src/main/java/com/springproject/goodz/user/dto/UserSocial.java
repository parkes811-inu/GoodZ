package com.springproject.goodz.user.dto;

import java.util.Date;

import lombok.Data;

@Data
public class UserSocial {
    private String userId;
    private String username;
    private String nickname;
    private String provider;
    private String socialId;
    private String picture;
    private Date createdAt;
    private Date updatedAt;
}
