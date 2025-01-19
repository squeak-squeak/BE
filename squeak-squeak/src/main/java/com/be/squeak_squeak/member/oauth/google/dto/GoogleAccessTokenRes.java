package com.be.squeak_squeak.member.oauth.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleAccessTokenRes (
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("expires_in")
        Long expiresIn,
        @JsonProperty("token_type")
        String tokenType
){
}