package com.springproject.goodz.product.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class UpdateProductRequest {
    private int pNo;
    private String bName;
    private String category;
    private String productName;
    private int initialPrice;
    private Map<String, String> optionPrices;
    private Map<String, String> addedStockQuantities;
    private Map<String, String> status;
    private Map<String, Integer> optionPNo;
    private Map<String, String> sizes;
}
