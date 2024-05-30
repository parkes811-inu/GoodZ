package com.springproject.goodz.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springproject.goodz.user.dto.UserAuth;
import com.springproject.goodz.user.dto.Users;
import com.springproject.goodz.user.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public boolean login(Users user) throws Exception {
        // // 💍 토큰 생성
        String username = user.getUserId();    // 아이디
        String password = user.getPassword();    // 암호화되지 않은 비밀번호
        UsernamePasswordAuthenticationToken token 
            = new UsernamePasswordAuthenticationToken(username, password);
        
        // 토큰을 이용하여 인증
        Authentication authentication = authenticationManager.authenticate(token);

        // 인증 여부 확인
        boolean result = authentication.isAuthenticated();

        // 시큐리티 컨텍스트에 등록
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return result;
    }

    @Override
    public Users select(String username) throws Exception {
        Users user = userMapper.select(username);
        return user;
    }

    @Override
    public int join(Users user) throws Exception {
        String username = user.getUserId();
        String password = user.getPassword();
        String encodedPassword = passwordEncoder.encode(password);  // 🔒 비밀번호 암호화
        user.setPassword(encodedPassword);

        // 회원 등록
        int result = userMapper.join(user);

        if( result > 0 ) {
            // 회원 기본 권한 등록
            UserAuth userAuth = new UserAuth();
            userAuth.setUserId(username);
            userAuth.setAuth("ROLE_USER");
            result = userMapper.insertAuth(userAuth);
        }
        return result;
    }

    @Override
    public int update(Users user) throws Exception {
        int result = userMapper.update(user);
        return result;
    }

    @Override
    public int insertAuth(UserAuth userAuth) throws Exception {
        int result = userMapper.insertAuth(userAuth);
        return result;
    }

    @Override
    public String findId(String phone, String name) throws Exception {
        String id = userMapper.findId(phone, name);
        return id;
    }

    @Override
    public String findPw(String username, String birth, String userId) throws Exception {
        String pw = userMapper.findPw(username, birth, userId);
        return pw;
    } 

    @Override
    public boolean check(String userId, String nickname) throws Exception {
        Integer result = userMapper.check(userId, nickname);
        System.out.println("Check Result: " + result); // 로깅 추가
        return result != null && result == 0; // null 체크 추가
    }
    
    @Override
    public boolean checkPassword(String userId, String rawPassword) throws Exception {
        Users user = userMapper.select(userId);
        if (user != null) {
            return passwordEncoder.matches(rawPassword, user.getPassword());
        }
        return false;
    }

    @Override
    public Users findUserByUsername(String username) throws Exception {
        return userMapper.select(username); // 'select' 메서드를 재사용하여 사용자 정보 조회
    }

  
}