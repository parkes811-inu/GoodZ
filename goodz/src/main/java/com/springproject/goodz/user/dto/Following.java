package com.springproject.goodz.user.dto;

import lombok.Data;

@Data
public class Following {
    private int followingNo;
    private String userId;
    private String followingId;
}
