package com.be.squeak_squeak.member.dto;

import com.be.squeak_squeak.member.entity.SocialType;

public record JoinReq(
        String accessToken,
        String phoneNumber,
        SocialType socialType
) {
}
