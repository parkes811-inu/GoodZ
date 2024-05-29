package com.springproject.goodz.pay.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Shipment {
    private int shipmentNo;         // 배송 번호
    private int purchaseNo;         // 구매 번호
    private String userId;          // 유저 아이디
    private String trackingNo;      // 운송장 번호
    private String shipmentState;   // 배송 상태 ENUM('pending', 'shipped', 'in_transit', 'delivered', 'returned')
    private Date shippedDate;       // 배송 날짜
    private Date deliverdDate;      // 배송 도착 날짜

}
