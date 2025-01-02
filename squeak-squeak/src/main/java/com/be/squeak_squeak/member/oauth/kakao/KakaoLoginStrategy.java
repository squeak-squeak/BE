package com.be.squeak_squeak.member.oauth.kakao;

import com.be.squeak_squeak.member.dto.LoginReq;
import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.entity.SocialType;
import com.be.squeak_squeak.member.oauth.SocialLoginStrategy;
import com.be.squeak_squeak.member.oauth.kakao.dto.KakaoProfileResponse;
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

        KakaoProfileResponse kakaoProfileResponse = kakaoApiClient.findProfiles(loginReq.accessToken());
        Member member = memberRepository.findBySocialUuid(kakaoProfileResponse.id()).orElseThrow(
                () -> new IllegalArgumentException("회원가입이 필요합니다.")
        );

        //이미 회원가입한 소셜 계정으로 로그인하지 않은 경우
        if (!kakaoProfileResponse.id().equals(member.getSocialUuid())) {
            throw new IllegalStateException("이미 회원가입된 계정이 존재합니다");
        }

        return member;
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
}
