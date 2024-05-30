package com.springproject.goodz.user.service;

import java.util.List;

import org.springframework.ui.Model;

import com.springproject.goodz.user.dto.Shippingaddress;
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
    
    // 비밀번호 찾기
    public Users findPw(String name, String birth, String userId ) throws Exception;

    // 비밀번호 변경
    public int changePw(String newPw, String userId) throws Exception;

    // 회원 가입 시 아이디 중복 체크
    public boolean check(String userId, String nickname) throws Exception;

    // 유저 정보 조회
    public Users findUserByUsername(String username) throws Exception;

    // 회원 정보 수정 시 비밀 번호 확인
    public boolean checkPassword(String userId, String rawPassword) throws Exception;

    // 유저의 주소 목록
    public List<Shippingaddress> selectByUserId() throws Exception;

}