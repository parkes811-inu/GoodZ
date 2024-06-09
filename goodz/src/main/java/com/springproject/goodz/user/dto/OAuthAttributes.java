package com.springproject.goodz.user.dto;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
public class OAuthAttributes {
    private Map<String, Object> attributes;     // OAuth 토큰 속성들
    private String nameAttributeKey;            // 사용자 이름 속성 키
    private String username;                    // 이름(닉네임)
    private String nickname;                    // 닉네임
    private String picture;                     // 프로필 사진 URL
    private String userId;                      // 사용자 정보 식별키

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String username, String nickname, String picture, String userId) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.username = username;
        this.nickname = nickname;
        this.picture = picture;
        this.userId = userId;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if ("kakao".equals(registrationId)) {
            return ofKakao(userNameAttributeName, attributes);
        }

        // 다른 OAuth 제공자들에 대한 설정 추가 가능
        return null;
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        System.out.println("ofKakao==" + attributes);
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        log.info("kakaoAccount : " + kakaoAccount);
        log.info("profile : " + profile);
        log.info("thumbnail_image_url : " + profile.get("thumbnail_image_url"));
        log.info("nickname : " + profile.get("nickname"));

        return OAuthAttributes.builder()
                .username((String) profile.get("nickname"))
                .nickname((String) profile.get("nickname"))
                .userId(String.valueOf(attributes.get(userNameAttributeName)))
                .picture((String) profile.get("thumbnail_image_url"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
}
