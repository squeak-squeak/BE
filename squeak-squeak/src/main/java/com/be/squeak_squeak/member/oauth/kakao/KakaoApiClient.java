package com.be.squeak_squeak.member.oauth.kakao;

import com.be.squeak_squeak.member.oauth.kakao.dto.KakaoProfileResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoApiClient {
    private static final String KAKAO_API_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String ACCESS_TOKEN_PREFIX = "BEARER ";

    public KakaoProfileResponse findProfiles(String accessToken) {
        //Http 요청
        WebClient wc = WebClient.create(KAKAO_API_URL);
        String response = wc.post()
                .uri(KAKAO_API_URL)
                .header("Authorization", ACCESS_TOKEN_PREFIX + accessToken)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfileResponse kakaoProfileResponse = null;

        try {
            kakaoProfileResponse = objectMapper.readValue(response, KakaoProfileResponse.class);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfileResponse;
    }
}