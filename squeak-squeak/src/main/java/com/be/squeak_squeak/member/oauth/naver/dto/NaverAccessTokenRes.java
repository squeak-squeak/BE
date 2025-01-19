package com.be.squeak_squeak.member.oauth.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NaverAccessTokenRes(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("refresh_token")
        String refreshToken,
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("expires_in")
        String expiresIn
) {
}
