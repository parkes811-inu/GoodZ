package com.springproject.goodz.user.dto;

import lombok.Data;

@Data
public class UserAuth {
    private int authNo;
    private String userId;
    private String auth;        // ROLE_USER, ROLE_ADMIN ...
}