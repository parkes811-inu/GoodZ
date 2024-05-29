package com.springproject.goodz.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.springproject.goodz.user.dto.UserAuth;
import com.springproject.goodz.user.dto.Users;

@Mapper
public interface UserMapper {

    // 로그인
    public Users login(String username) throws Exception;

    // 회원 조회
    public Users select(String username) throws Exception;

    // 회원 가입
    public int join(Users user) throws Exception;

    // 회원 수정
    public int update(Users user) throws Exception;

    // 회원 권한 등록
    public int insertAuth(UserAuth userAuth) throws Exception;

    // 아이디 찾기
    public String findId(@Param("phoneNumber") String phone, @Param("userName") String name) throws Exception;
    
    // 비밀번호 찾기
    public Users findPw(@Param("userName") String name , @Param("birth") String birth , @Param("userId") String userId) throws Exception;
    
    // 비밀번호 변경
    public int changePw(@Param("password") String password, @Param("userId") String userId) throws Exception;

    // 회원 가입 시 아이디 중복 체크
    public int checkId(@Param("userId") String userId) throws Exception;


}