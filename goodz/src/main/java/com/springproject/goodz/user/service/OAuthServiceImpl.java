package com.springproject.goodz.user.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springproject.goodz.user.dto.CustomUser;
import com.springproject.goodz.user.dto.OAuthAttributes;
import com.springproject.goodz.user.dto.SocialUser;
import com.springproject.goodz.user.dto.UserAuth;
import com.springproject.goodz.user.dto.UserSocial;
import com.springproject.goodz.user.dto.Users;
import com.springproject.goodz.user.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OAuthServiceImpl extends DefaultOAuth2UserService implements OAuthService {

    @Autowired
    private UserMapper userMapper;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("::::::::::::::: OAuthServiceImpl - loadUser() :::::::::::::::");
        log.info("OAuth 사용자 정보를 전달받아 OAuth2User 객체로 변환합니다.");

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                                .getUserInfoEndpoint().getUserNameAttributeName();

        log.info("★★★★★ 주요 정보 ★★★★★");
        log.info("****** registrationId : " + registrationId);
        log.info("****** userNameAttributeName : " + userNameAttributeName);
        log.info("****** attributes : " + attributes);

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);
        log.info("****** oAuthAttributes : " + oAuthAttributes);

        String provider = registrationId;
        String socialId = oAuthAttributes.getUserId();

        log.info(":::::::::::::::::::::::::::::::::::::::::::::");
        log.info(provider + "로 로그인 합니다.");
        log.info(":::::::::::::::::::::::::::::::::::::::::::::");

        UserSocial userSocial = new UserSocial();
        userSocial.setProvider(provider);
        userSocial.setSocialId(socialId);
        userSocial.setUsername(oAuthAttributes.getUsername());
        // userSocial.setNickname(oAuthAttributes.getNickname());
        userSocial.setPicture(oAuthAttributes.getPicture());

        log.info("======================================================");
        log.info("시발 진짜 좀 되라 제발 : " + userSocial);
        log.info("======================================================");

        Users joinedUser = null;
        try {
            joinedUser = userMapper.selectBySocial(userSocial);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (joinedUser == null) {
            log.info("***** 소셜 회원 가입 *****");
            try {
                join(userSocial, oAuthAttributes);
                joinedUser = userMapper.selectBySocial(userSocial); // 회원 가입 후 다시 조회
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            log.info("***** 소셜 회원 정보 갱신 *****");
            log.info("joinedUser : " + joinedUser);

            UserSocial joinedUserSocial = null;
            try {
                joinedUserSocial = userMapper.selectSocial(userSocial);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (joinedUserSocial != null) {
                try {
                    update(joinedUserSocial, oAuthAttributes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (joinedUser == null) {
            throw new OAuth2AuthenticationException("가입된 소셜 사용자가 없습니다.");
        }

        log.info("***** 가입된 소셜 사용자 정보 *****");
        log.info(joinedUser.toString());
        CustomUser customUser = new CustomUser(joinedUser);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities()));

        // response 속성 추가
        Map<String, Object> attributesWithResponse = new java.util.HashMap<>(oAuthAttributes.getAttributes());
        attributesWithResponse.put("response", attributes);

        return new SocialUser(joinedUser, attributesWithResponse);
    }

    @Override
    public int join(UserSocial userSocial, OAuthAttributes oAuthAttributes) throws Exception {
        Users joinedUser = userMapper.selectBySocial(userSocial);
        int result = 0;
        if (joinedUser == null) {
            String userId = userSocial.getProvider() + "_";
            
            if(userSocial.getSocialId().length() > 10) {
                userId = userId + userSocial.getSocialId().substring(4, 10);
            } else {
                userId = userId + userSocial.getSocialId();
            }

            Users user = new Users();
            user.setUserId(userId);
            user.setUsername(userSocial.getUsername());
            user.setNickname(userId);
            user.setProfilePictureUrl(userSocial.getPicture());
            user.setPassword(UUID.randomUUID().toString());

            log.info("======================================================");
            log.info("User for joining: " + user);
            log.info("======================================================");

            result = userMapper.join(user);

            UserAuth userAuth = new UserAuth();
            userAuth.setAuth("ROLE_USER");
            userAuth.setUserId(userId);
            userMapper.insertAuth(userAuth);

            UserSocial newUserSocial = new UserSocial();
            newUserSocial.setProvider(userSocial.getProvider());
            newUserSocial.setSocialId(userSocial.getSocialId());
            newUserSocial.setUserId(userId);
            newUserSocial.setUsername(userSocial.getUsername());
            newUserSocial.setNickname(userId);
            newUserSocial.setPicture(userSocial.getPicture());

            result += userMapper.insertSocial(newUserSocial);
        }
        return result;
    }

    @Override
    public int update(UserSocial userSocial, OAuthAttributes oAuthAttributes) throws Exception {
        int result = 0;

        String username = userSocial.getUsername();
        String picture = userSocial.getPicture();

        if (!username.equals(oAuthAttributes.getUsername())) username = oAuthAttributes.getUsername();
        if (!picture.equals(oAuthAttributes.getPicture())) picture = oAuthAttributes.getPicture();

        userSocial.setUsername(username);
        userSocial.setPicture(picture);

        result = userMapper.updateSocial(userSocial);

        return result;
    }
}
