package com.springproject.goodz.user.dto;

import lombok.Data;

@Data
public class Follower {
    private int followerNo;
    private String userId;
    private String followerId;
}
