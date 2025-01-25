package com.be.squeak_squeak.group.service;

import com.be.squeak_squeak.group.dto.CreateGroupReq;
import com.be.squeak_squeak.group.dto.CreateGroupRes;
import com.be.squeak_squeak.group.entity.UserGroup;
import com.be.squeak_squeak.group.repository.UserGroupRepository;
import com.be.squeak_squeak.groupMember.entity.GroupMember;
import com.be.squeak_squeak.groupMember.entity.MemberStatus;
import com.be.squeak_squeak.groupMember.repository.GroupMemberRepository;
import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGroupService {
    private final UserGroupRepository userGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;

    public CreateGroupRes createGroup(CreateGroupReq request, Long memberId) {
        // 초대 코드 생성
        // String inviteCode = generateInviteCode();

        Member creator = memberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        UserGroup group = UserGroup.builder()
                .name(request.name())
                .image(request.image())
                .description(request.description())
                .totalMemberCount(1) // 그룹 생성 시 1명 (그룹장)
                .inviteCode("임시 초대 코드")
                .build();

        // 그룹 저장
        userGroupRepository.save(group);

        GroupMember groupMember = GroupMember.builder()
                .userGroup(group)
                .status(MemberStatus.OWNER)
                .member(creator)
                .build();

        groupMemberRepository.save(groupMember);

        return CreateGroupRes.builder()
                .id(group.getId())
                .name(group.getName())
                .image(group.getImage())
                .description(group.getDescription())
                .inviteCode(group.getInviteCode())
                .build();
    }

    private String generateInviteCode() {
        return null;
    }
}
