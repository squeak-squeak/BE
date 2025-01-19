package com.be.squeak_squeak.member.oauth.google;

import com.be.squeak_squeak.member.oauth.google.dto.GoogleAccessTokenRes;
import com.be.squeak_squeak.member.oauth.google.dto.GoogleMemberInfoRes;
import io.netty.handler.codec.http.HttpHeaderValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GoogleApiClient {

    @Value("${google.member-info-url}")
    private String GOOGLE_MEMBER_INFO_URL;
    @Value("${google.access-token-url}")
    private String GOOGLE_ACCESS_TOKEN_URL;
    @Value("${google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${google.secret-id}")
    private String GOOGLE_SECRET_ID;
    @Value("${google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;

    public GoogleAccessTokenRes getAccessToken(String code) {
        return WebClient.create(GOOGLE_ACCESS_TOKEN_URL).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", GOOGLE_CLIENT_ID)
                        .queryParam("client_secret", GOOGLE_SECRET_ID)
                        .queryParam("redirect_uri", GOOGLE_REDIRECT_URI)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO: Custom Exception
                .bodyToMono(GoogleAccessTokenRes.class)
                .block();
    }

    public GoogleMemberInfoRes getUserInfo(String accessToken) {
        return WebClient.create(GOOGLE_MEMBER_INFO_URL).get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/userinfo")
                        .build(true))
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                //TODO: Custom Exception
                .bodyToMono(GoogleMemberInfoRes.class)
                .block();
    }
}