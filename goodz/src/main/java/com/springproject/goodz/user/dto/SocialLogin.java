package com.springproject.goodz.user.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SocialLogin {
    private String socialLoginId;
    private String userId;
    private String provider;
    private String providerUserId;
    private Date createdAt;
}
