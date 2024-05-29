package com.springproject.goodz.user.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Shippingaddress {
    private int addressNo;          // 주소 번호
    private String userId;          // 유저 아이디
    private String recipientName;   // 수취인
    private String address;         // 주소
    private String zipCode;         // 우편 번호
    private String phoneNumber;     // 핸드폰 번호
    private Boolean isDefault;      // 기본 배송지 여부
    private String type;            // 구매/반송 ENUM('return','buy')
    private Date createdAt;         
    private Date updatedAt;

}
