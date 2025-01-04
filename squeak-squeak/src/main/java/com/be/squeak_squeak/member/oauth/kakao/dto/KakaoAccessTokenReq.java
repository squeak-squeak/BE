package com.be.squeak_squeak.member.oauth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record KakaoAccessTokenReq(
        @JsonProperty("grant_type")
        String grantType,
        @JsonProperty("client_id")
        String clientId,
        @JsonProperty("redirect_uri")
        String redirectUri,
        @JsonProperty("code")
        String code
) {
}
