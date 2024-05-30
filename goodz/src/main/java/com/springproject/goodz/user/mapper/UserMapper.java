package com.springproject.goodz.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.ui.Model;

import com.springproject.goodz.user.dto.Shippingaddress;
import com.springproject.goodz.user.dto.UserAuth;
import com.springproject.goodz.user.dto.Users;

@Mapper
public interface UserMapper {

    // 로그인
    public Users login(String username) throws Exception;

    // 회원 조회 - id
    public Users select(String username) throws Exception;

    // 회원 조회 - id
    public Users selectByNickname(String nickname) throws Exception;

    // 회원 가입
    public int join(Users user) throws Exception;

    // 회원 수정
    public int update(Users user) throws Exception;

    // 회원 권한 등록
    public int insertAuth(UserAuth userAuth) throws Exception;

    // 아이디 찾기
    public String findId(@Param("phoneNumber") String phone, @Param("userName") String name) throws Exception;
    
    // 비밀번호 찾기
    public Users findPw(@Param("userName") String name, @Param("birth") String birth , @Param("userId") String userId) throws Exception;

    // 회원 가입, 정보 수정 시 아이디 및 닉네임 중복 체크
    public Integer check(@Param("userId") String userId, @Param("nickname") String nickname) throws Exception;
  
    // 비밀번호 변경
    public int changePw(@Param("password") String password, @Param("userId") String userId) throws Exception;

    // 회원 정보 수정 시 닉네임 중복 체크
    public int checkName(@Param("userName") String userName) throws Exception;

    // 유저의 주소 목록
    public List<Shippingaddress> selectByUserId() throws Exception;
    
}