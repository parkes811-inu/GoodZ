package com.springproject.goodz.user.dto;

import lombok.Data;

@Data
public class Follow {

    private int no;                     // 고유번호
    private String userId;              // 프로필 아이디

    private String followerId;          // 팔로워 아이디
    private String followerNickname;    // 팔로워 닉네임
    
    private String followingId;         // 팔로워 아이디
    private String followingNickname;   // 팔로워 닉네임
    
    private int followerCount;          // 팔로워 수
    private int followingCount;         // 팔로잉 수


    /** 예시
     * 
     * 김도희의 팔로워 김구라, 김준현
     * => userId = "김도희" followerId = "김구라" / userId = "김도희" followerId = "김준현"
     * 
     * 김도희의 팔로잉 목록
     * => userId = "김태희" followerId = "김도희" /  userId="백지영" followerId = "김도희"
     */
}
