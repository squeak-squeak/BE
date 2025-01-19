package com.be.squeak_squeak.member.oauth.naver;

import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.entity.SocialType;
import com.be.squeak_squeak.member.oauth.SocialLoginStrategy;
import com.be.squeak_squeak.member.oauth.dto.LoginReq;
import com.be.squeak_squeak.member.oauth.naver.dto.NaverAccessTokenRes;
import com.be.squeak_squeak.member.oauth.naver.dto.NaverMemberInfoRes;
import com.be.squeak_squeak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverLoginStrategy implements SocialLoginStrategy {

    private final NaverApiClient naverApiClient;
    private final MemberRepository memberRepository;

    @Override
    public Member login(LoginReq loginReq) {
        NaverAccessTokenRes accessTokenRes = naverApiClient.getAccessToken(loginReq.code(), loginReq.state());
        NaverMemberInfoRes naverMemberInfoRes = naverApiClient.getUserInfo(accessTokenRes.accessToken());
        return memberRepository.findBySocialUuid(naverMemberInfoRes.response().id()).orElseThrow(
                () -> new IllegalArgumentException("회원가입이 필요합니다.")
        );
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.NAVER;
    }
}
