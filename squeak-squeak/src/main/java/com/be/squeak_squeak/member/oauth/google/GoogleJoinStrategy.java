package com.be.squeak_squeak.member.oauth.google;

import com.be.squeak_squeak.member.oauth.dto.JoinReq;
import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.entity.SocialType;
import com.be.squeak_squeak.member.oauth.SocialJoinStrategy;
import com.be.squeak_squeak.member.oauth.google.dto.GoogleAccessTokenRes;
import com.be.squeak_squeak.member.oauth.google.dto.GoogleMemberInfoRes;
import com.be.squeak_squeak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class GoogleJoinStrategy implements SocialJoinStrategy {

    private final GoogleApiClient googleApiClient;
    private final MemberRepository memberRepository;

    @Override
    public Member join(JoinReq joinReq) {
        memberRepository.findByPhoneNumber(joinReq.phoneNumber()).ifPresent(
                member -> {
                    throw new IllegalArgumentException("이미 가입된 회원입니다.");
                }
        );

        GoogleAccessTokenRes accessTokenRes = googleApiClient.getAccessToken(joinReq.code());
        GoogleMemberInfoRes response = googleApiClient.getUserInfo(accessTokenRes.accessToken());
        return Member.builder()
                .nickname(response.name()) // 구글 사용자 이름
                .image("기본이미지") // 기본 이미지 설정
                .email(response.email()) // 구글 이메일
                .phoneNumber(joinReq.phoneNumber())
                .socialType(SocialType.GOOGLE) // 소셜 타입을 구글로 설정
                .socialUuid(response.id()) // 구글 사용자 ID
                .build();
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.GOOGLE; // 소셜 타입을 구글로 반환
    }
}