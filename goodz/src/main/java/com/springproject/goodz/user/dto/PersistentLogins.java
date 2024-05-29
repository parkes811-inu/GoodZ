package com.springproject.goodz.user.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PersistentLogins {
    
    private String username;
    private String series;
    private String token;
    private Date lastUsed;
}
