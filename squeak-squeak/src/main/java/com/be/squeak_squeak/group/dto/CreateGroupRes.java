package com.be.squeak_squeak.group.dto;

import lombok.Builder;

@Builder
public record CreateGroupRes(
        Long id,
        String name,
        String image,
        String description,
        String inviteCode
) {
}
