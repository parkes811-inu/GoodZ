package com.springproject.goodz.user.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Wish {
    private int wNo;
    private String userId;
    private String parentTable;
    private int parentNo;
    private Date createdAt;
}
