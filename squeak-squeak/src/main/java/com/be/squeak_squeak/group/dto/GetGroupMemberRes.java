package com.be.squeak_squeak.group.dto;

import lombok.Builder;

@Builder
public record GetGroupMemberRes(
        Long memberId,
        String name,
        String image
) {
}