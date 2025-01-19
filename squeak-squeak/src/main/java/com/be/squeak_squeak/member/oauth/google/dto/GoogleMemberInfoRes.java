package com.be.squeak_squeak.member.oauth.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleMemberInfoRes (
        @JsonProperty("sub") String id,
        @JsonProperty("name") String name,
        @JsonProperty("email") String email
){
}