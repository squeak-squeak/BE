package com.be.squeak_squeak.member.oauth;

import com.be.squeak_squeak.member.oauth.dto.JoinReq;
import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.entity.SocialType;

public interface SocialJoinStrategy {
    Member join(JoinReq joinReq);

    SocialType getSocialType();
}
