package com.be.squeak_squeak.member.oauth;

import com.be.squeak_squeak.common.security.TokenGenerator;
import com.be.squeak_squeak.member.dto.JwtAccessTokenRes;
import com.be.squeak_squeak.member.dto.LoginReq;
import com.be.squeak_squeak.member.dto.JoinReq;
import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.entity.SocialType;
import com.be.squeak_squeak.member.repository.MemberRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuthService {

    private final MemberRepository memberRepository;
    private final TokenGenerator tokenGenerator;
    private final Map<SocialType, SocialLoginStrategy> strategies;
    private final Map<SocialType, SocialJoinStrategy> joinStrategies;

    @Autowired
    public OAuthService(MemberRepository memberRepository,
                        TokenGenerator tokenGenerator,
                        List<SocialLoginStrategy> loginStrategies,
                        List<SocialJoinStrategy> joinStrategyList) {
        this.memberRepository = memberRepository;
        this.tokenGenerator = tokenGenerator;
        this.strategies = loginStrategies.stream()
                .collect(Collectors.toMap(
                        SocialLoginStrategy::getSocialType,
                        strategy -> strategy
                ));
        this.joinStrategies = joinStrategyList.stream()
                .collect(Collectors.toMap(
                        SocialJoinStrategy::getSocialType,
                        strategy -> strategy
                ));
    }

    @Transactional(readOnly = true)
    public JwtAccessTokenRes login(LoginReq loginReq) {
        SocialLoginStrategy strategy = strategies.get(loginReq.socialType());
        Member member = strategy.login(loginReq);
        return new JwtAccessTokenRes(tokenGenerator.createAccessToken(member));
    }

    @Transactional
    public JwtAccessTokenRes join(JoinReq joinReq) {
        Member member = joinStrategies.get(joinReq.socialType()).join(joinReq);
        memberRepository.save(member);
        return new JwtAccessTokenRes(tokenGenerator.createAccessToken(member));
    }


}

