package com.be.squeak_squeak.member.controller;


import com.be.squeak_squeak.common.config.ApiResponse;
import com.be.squeak_squeak.common.config.ApiResponse.CustomBody;
import com.be.squeak_squeak.common.config.ApiResponseGenerator;
import com.be.squeak_squeak.member.oauth.dto.JwtAccessTokenRes;
import com.be.squeak_squeak.member.oauth.dto.LoginReq;
import com.be.squeak_squeak.member.oauth.dto.JoinReq;
import com.be.squeak_squeak.member.oauth.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final OAuthService oauthService;

    /*
     * 회원 중복 확인 / 로그인
     */
    @PostMapping("/login")
    public ApiResponse<CustomBody<JwtAccessTokenRes>> login(@RequestBody LoginReq loginReq) {
        JwtAccessTokenRes accessTokenRes = oauthService.login(loginReq);
        return ApiResponseGenerator.success(accessTokenRes, HttpStatus.OK);
    }

    /*
     * 회원 가입 / 로그인
     */
    @PostMapping("/join")
    public ApiResponse<CustomBody<JwtAccessTokenRes>> join(@RequestBody JoinReq joinReq) {
        JwtAccessTokenRes accessTokenRes = oauthService.join(joinReq);
        return ApiResponseGenerator.success(accessTokenRes, HttpStatus.OK);
    }

}
