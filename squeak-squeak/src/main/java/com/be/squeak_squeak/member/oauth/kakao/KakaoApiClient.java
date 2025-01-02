package com.be.squeak_squeak.member.oauth.kakao;

import com.be.squeak_squeak.member.oauth.kakao.dto.KakaoProfileResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoApiClient {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public KakaoApiClient(@Qualifier("kakaoWebClient") WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    @Value("${kakao.api.url}")
    private String KAKAO_API_URL;

    public KakaoProfileResponse findProfiles(String accessToken) {
        String response = webClient.post()
                .uri(KAKAO_API_URL)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            return objectMapper.readValue(response, KakaoProfileResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("응답을 받아오는 과정에서 오류가 발생했습니다.", e);
        }
    }
}