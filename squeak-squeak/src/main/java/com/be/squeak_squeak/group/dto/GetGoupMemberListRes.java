package com.be.squeak_squeak.group.dto;

import java.util.List;

public record GetGoupMemberListRes(
        Boolean isOwner,
        int waitingCount,
        List<GetGroupMemberRes> groupMemberList
) {
}