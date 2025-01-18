package com.be.squeak_squeak.member.oauth.naver.dto;

public record NaverMemberInfoRes(
        Response response
) {
    public record Response(
            String id,
            String email,
            String nickname
    ) {
    }
}
