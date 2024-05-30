package com.springproject.goodz.user.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Wishlist {
    private int wNo;
    private String userId;
    private int pNo;
    private Date createdAt;
    private Date updatedAt;
}
