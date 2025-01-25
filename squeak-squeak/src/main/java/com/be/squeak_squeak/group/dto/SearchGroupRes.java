package com.be.squeak_squeak.group.dto;

import lombok.Builder;

@Builder
public record SearchGroupRes(
        Long id,
        String image,
        String name
) {
}
