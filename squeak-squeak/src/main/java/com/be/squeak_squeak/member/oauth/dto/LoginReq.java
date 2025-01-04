package com.be.squeak_squeak.member.oauth.dto;

import com.be.squeak_squeak.member.entity.SocialType;

public record LoginReq(
        String code,
        SocialType socialType
) {
}
