package com.be.squeak_squeak.member.oauth.kakao;

import com.be.squeak_squeak.member.oauth.kakao.dto.KakaoAccessTokenRes;
import com.be.squeak_squeak.member.oauth.kakao.dto.KakaoMemberInfoRes;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoApiClient {

    @Value("${kakao.member-info-url}")
    private String KAKAO_MEMBER_INFO_URL;
    @Value("${kakao.access-token-url}")
    private String KAKAO_ACCESS_TOKEN_URL;
    @Value("${kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    public KakaoMemberInfoRes getUserInfo(String accessToken) {
        return WebClient.create(KAKAO_MEMBER_INFO_URL).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header("Authorization", "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO: Custom Exception
                .bodyToMono(KakaoMemberInfoRes.class)
                .block();
    }


    public KakaoAccessTokenRes getAccessToken(String code) {
        System.out.println(KAKAO_ACCESS_TOKEN_URL);
        return WebClient.create(KAKAO_ACCESS_TOKEN_URL).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", KAKAO_CLIENT_ID)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .bodyToMono(KakaoAccessTokenRes.class)
                .block();

    }
}