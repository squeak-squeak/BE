package com.be.squeak_squeak.member.oauth;

import com.be.squeak_squeak.member.oauth.dto.LoginReq;
import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.entity.SocialType;

public interface SocialLoginStrategy {
    Member login(LoginReq loginReq);

    SocialType getSocialType();
}
