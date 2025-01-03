package com.be.squeak_squeak.member.oauth.kakao;

import com.be.squeak_squeak.member.oauth.dto.LoginReq;
import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.entity.SocialType;
import com.be.squeak_squeak.member.oauth.SocialLoginStrategy;
import com.be.squeak_squeak.member.oauth.kakao.dto.KakaoAccessTokenRes;
import com.be.squeak_squeak.member.oauth.kakao.dto.KakaoMemberInfoRes;
import com.be.squeak_squeak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KakaoLoginStrategy implements SocialLoginStrategy {

    private final KakaoApiClient kakaoApiClient;
    private final MemberRepository memberRepository;

    @Override
    public Member login(LoginReq loginReq) {
        KakaoAccessTokenRes accessTokenRes = kakaoApiClient.getAccessToken(loginReq.code());
        KakaoMemberInfoRes kakaoMemberInfoRes = kakaoApiClient.getUserInfo(accessTokenRes.accessToken());
        return memberRepository.findBySocialUuid(kakaoMemberInfoRes.id()).orElseThrow(
                () -> new IllegalArgumentException("회원가입이 필요합니다.")
        );
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
}
