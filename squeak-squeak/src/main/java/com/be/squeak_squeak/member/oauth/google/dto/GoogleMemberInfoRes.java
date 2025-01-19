package com.be.squeak_squeak.member.oauth.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleMemberInfoRes (
        @JsonProperty("id") String id,
        @JsonProperty("email") String email,
        @JsonProperty("name") String name
){
}