package com.be.squeak_squeak.member.oauth.kakao.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoMemberInfoRes(
        @JsonProperty("id")
        String id,
        @JsonProperty("connected_at")
        String connectedAt,
        @JsonProperty("properties")
        Properties properties,
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) {
    public record Properties(
            @JsonProperty("nickname")
            String nickname
    ) {
    }

    public record KakaoAccount(
            @JsonProperty("profile")
            Profile profile,
            @JsonProperty("email")
            String email
    ) {
        public record Profile(
                @JsonProperty("nickname")
                String nickname
        ) {
        }
    }
}