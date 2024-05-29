package com.springproject.goodz.pay.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Inspection {
    private int iNo;            // 검수 번호
    private int sNo;            // 판매 번호
    private String insState;    // 검수 상태 ENUM('pending', 'verified', 'rejected')
    private Date insDate;       // 검수 날짜
}
