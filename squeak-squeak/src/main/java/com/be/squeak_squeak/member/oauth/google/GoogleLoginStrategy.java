package com.be.squeak_squeak.member.oauth.google;

import com.be.squeak_squeak.member.oauth.dto.LoginReq;
import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.entity.SocialType;
import com.be.squeak_squeak.member.oauth.SocialLoginStrategy;
import com.be.squeak_squeak.member.oauth.google.dto.GoogleAccessTokenRes;
import com.be.squeak_squeak.member.oauth.google.dto.GoogleMemberInfoRes;
import com.be.squeak_squeak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoogleLoginStrategy implements SocialLoginStrategy {

    private final GoogleApiClient googleApiClient;
    private final MemberRepository memberRepository;

    @Override
    public Member login(LoginReq loginReq) {
        GoogleAccessTokenRes accessTokenRes = googleApiClient.getAccessToken(loginReq.code());
        GoogleMemberInfoRes googleUserInfoRes = googleApiClient.getUserInfo(accessTokenRes.accessToken());
        return memberRepository.findBySocialUuid(googleUserInfoRes.id()).orElseThrow(
                () -> new IllegalArgumentException("회원가입이 필요합니다.")
        );
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.GOOGLE; // 소셜 타입을 구글로 반환
    }
}