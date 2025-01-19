package com.be.squeak_squeak.member.oauth.naver;

import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.entity.SocialType;
import com.be.squeak_squeak.member.oauth.SocialJoinStrategy;
import com.be.squeak_squeak.member.oauth.dto.JoinReq;
import com.be.squeak_squeak.member.oauth.naver.dto.NaverAccessTokenRes;
import com.be.squeak_squeak.member.oauth.naver.dto.NaverMemberInfoRes;
import com.be.squeak_squeak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NaverJoinStrategy implements SocialJoinStrategy {

    private final MemberRepository memberRepository;
    private final NaverApiClient naverApiClient;

    @Override
    public Member join(JoinReq joinReq) {
        memberRepository.findByPhoneNumber(joinReq.phoneNumber()).ifPresent(
                member -> {
                    throw new IllegalArgumentException("이미 가입된 회원입니다.");
                }
        );

        NaverAccessTokenRes accessTokenRes = naverApiClient.getAccessToken(joinReq.code(), joinReq.state());
        System.out.println(accessTokenRes);
        NaverMemberInfoRes response = naverApiClient.getUserInfo(accessTokenRes.accessToken());
        return Member.builder()
                .nickname(response.response().nickname())
                .image("기본이미지")
                .email(response.response().email())
                .phoneNumber(joinReq.phoneNumber())
                .socialType(SocialType.NAVER)
                .socialUuid(response.response().id())
                .build();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.NAVER;
    }
}
