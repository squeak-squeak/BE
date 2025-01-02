package com.be.squeak_squeak.member.dto;

import com.be.squeak_squeak.member.entity.SocialType;

public record LoginReq(
        String accessToken,
        SocialType socialType
) {
}
