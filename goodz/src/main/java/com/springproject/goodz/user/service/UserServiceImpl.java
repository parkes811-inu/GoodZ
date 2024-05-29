package com.springproject.goodz.user.service;

import java.util.HashMap;
import java.util.Map;

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

import lombok.extern.slf4j.Slf4j;
@Slf4j
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
    public Users findPw(String username, String birth, String userId) throws Exception {
        log.info("findPw 메소드 호출: username={}, birth={}, userId={}", username, birth, userId);
        Users findMan = userMapper.findPw(username, birth, userId);

        return findMan;
    } 
    // @Override
    // public String findPw(String username, String birth, String userId) throws Exception {
    //     log.info("findPw 메소드 호출: username={}, birth={}, userId={}", username, birth, userId);
    //     String pw = userMapper.findPw(username, birth, userId);
    //     log.info("findPw 결과: {}", pw);
    //     return pw;
    // } 
    
    @Override
    public int changePw(String newPw, String userId) throws Exception {
        // log.info("changePw 메소드 호출: username={}, currentPw={}, newPw={}", username, currentPw, newPw);
        // 사용자의 현재 비밀번호를 가져옵니다.
        
        // 비밀번호가 일치하는지 확인합니다.
            // 새 비밀번호를 암호화하여 업데이트합니다.
            String password = passwordEncoder.encode(newPw);
            log.info("새로운 비밀번호 암호화 결과: {}", password);

            int result = userMapper.changePw(password, userId);
            if(result > 0) {
                log.info("비밀번호 변경 성공");
                return result; // 성공
            }
            else {
                log.info("비밀번호 변경 123123실패");
                return 0;
            }
    }
    
    // @Override
    // public String findPw(String username, String birth, String userId) throws Exception {
    //     String pw = userMapper.findPw(username, birth, userId);
    //     return pw;
    // } 

    //  @Override
    //  public int changePw(String username, String currentPw, String newPw) throws Exception {
    //     log.info("임플진입");
    //      // 사용자의 현재 비밀번호를 가져옵니다.
    //      Users user = userMapper.select(username);
    //      // 비밀번호가 일치하는지 확인합니다.
    //      if (passwordEncoder.matches(currentPw, user.getPassword())) {
    //          // 새 비밀번호를 암호화하여 업데이트합니다.
    //          String encodedNewPw = passwordEncoder.encode(newPw);
    //          userMapper.changePw(username, encodedNewPw);
    //          return 1; // 성공
    //      } else {
    //          return 0; // 현재 비밀번호가 일치하지 않습니다.
    //      }
    //  }

    @Override
    public boolean checkId(String userId) throws Exception {
        int result = userMapper.checkId(userId);
        return result == 0;
    }

    

}