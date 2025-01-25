package com.be.squeak_squeak.common.config;

import com.be.squeak_squeak.common.auth.AuthenticatedMemberArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthenticatedMemberArgumentResolver authenticatedMemberArgumentResolver;


    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authenticatedMemberArgumentResolver);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*") // 허용할 도메인 설정
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메소드 설정
                .allowedHeaders("*") // 허용할 헤더 설정
                .allowCredentials(true)
                .maxAge(3600); // preflight 요청의 유효시간 설정

    }
}