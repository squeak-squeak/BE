package com.be.squeak_squeak.member.oauth.kakao.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoProfileResponse(
        String id,
        Properties properties,
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {
    public record Properties(
            String nickname
    ) {
    }

    public record KakaoAccount(
            Profile profile,
            String email
    ) {
        public record Profile(
                String nickname
        ) {
        }
    }
}