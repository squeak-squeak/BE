package com.be.squeak_squeak.member.oauth.naver;


import com.be.squeak_squeak.member.oauth.naver.dto.NaverAccessTokenRes;
import com.be.squeak_squeak.member.oauth.naver.dto.NaverMemberInfoRes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NaverApiClient {
    @Value("${naver.access-token.url}")
    private String NAVER_ACCESS_TOKEN_URL;
    @Value("${naver.member-info.url}")
    private String NAVER_MEMBER_INFO_URL;
    @Value("${naver.client-id}")
    private String NAVER_CLIENT_ID;
    @Value("${naver.client-secret}")
    private String NAVER_CLIENT_SECRET;

    public NaverMemberInfoRes getUserInfo(String accessToken) {
        return WebClient.create(NAVER_MEMBER_INFO_URL).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v1/nid/me")
                        .build(true))
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                //TODO: Custom Exception
                .bodyToMono(NaverMemberInfoRes.class)
                .block();
    }


    public NaverAccessTokenRes getAccessToken(String code, String state) {
        return WebClient.create(NAVER_ACCESS_TOKEN_URL).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth2.0/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", NAVER_CLIENT_ID)
                        .queryParam("client_secret", NAVER_CLIENT_SECRET)
                        .queryParam("code", code)
                        .queryParam("state", state)
                        .build(true))
                .retrieve()
                //TODO : Custom Exception
                .bodyToMono(NaverAccessTokenRes.class)
                .block();

    }
}
