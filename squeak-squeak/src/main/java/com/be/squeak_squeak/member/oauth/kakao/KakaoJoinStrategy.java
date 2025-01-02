package com.be.squeak_squeak.member.oauth.kakao;


import com.be.squeak_squeak.member.dto.JoinReq;
import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.entity.SocialType;
import com.be.squeak_squeak.member.oauth.SocialJoinStrategy;
import com.be.squeak_squeak.member.oauth.kakao.dto.KakaoProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class KakaoJoinStrategy implements SocialJoinStrategy {

    private final KakaoApiClient kakaoApiClient;

    @Override
    public Member join(JoinReq joinReq) {
        KakaoProfileResponse response = kakaoApiClient.findProfiles(joinReq.accessToken());
        return Member.builder()
                .nickname(response.properties().nickname())
                .image("")
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