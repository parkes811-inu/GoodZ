package com.springproject.goodz.user.service;

import com.springproject.goodz.user.dto.UserAuth;
import com.springproject.goodz.user.dto.Users;

public interface UserService {
    
    // 로그인
    public boolean login(Users user) throws Exception;

    // 조회
    public Users select(String username) throws Exception;

    // 회원 가입
    public int join(Users user) throws Exception;

    // 회원 수정
    public int update(Users user) throws Exception;

    // 회원 권한 등록
    public int insertAuth(UserAuth userAuth) throws Exception;

    // 아이디 찾기
    public String findId(String phone, String name) throws Exception;

    // 회원 가입 시 아이디 중복 체크
    public boolean checkId(String userId) throws Exception;

}