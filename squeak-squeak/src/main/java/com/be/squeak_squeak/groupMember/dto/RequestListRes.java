package com.be.squeak_squeak.groupMember.dto;

public record RequestListRes(
        Long memberId,
        String memberNickname,
        String groupName
) {
}
