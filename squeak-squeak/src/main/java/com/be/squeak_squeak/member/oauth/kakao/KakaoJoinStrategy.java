package com.be.squeak_squeak.member.oauth.kakao;


import com.be.squeak_squeak.member.oauth.dto.JoinReq;
import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.entity.SocialType;
import com.be.squeak_squeak.member.oauth.SocialJoinStrategy;
import com.be.squeak_squeak.member.oauth.kakao.dto.KakaoAccessTokenRes;
import com.be.squeak_squeak.member.oauth.kakao.dto.KakaoMemberInfoRes;
import com.be.squeak_squeak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class KakaoJoinStrategy implements SocialJoinStrategy {

    private final KakaoApiClient kakaoApiClient;
    private final MemberRepository memberRepository;

    @Override
    public Member join(JoinReq joinReq) {

        memberRepository.findByPhoneNumber(joinReq.phoneNumber()).ifPresent(
                member -> {
                    throw new IllegalArgumentException("이미 가입된 회원입니다.");
                }
        );

        KakaoAccessTokenRes accessTokenRes = kakaoApiClient.getAccessToken(joinReq.code());
        KakaoMemberInfoRes response = kakaoApiClient.getUserInfo(accessTokenRes.accessToken());
        return Member.builder()
                .nickname(response.properties().nickname())
                .image("기본이미지")
                .email(response.kakaoAccount().email())
                .phoneNumber(joinReq.phoneNumber())
                .socialType(SocialType.KAKAO)
                .socialUuid(response.id())
                .build();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }
}