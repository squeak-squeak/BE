package com.be.squeak_squeak.group.service;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.be.squeak_squeak.group.dto.CreateGroupReq;
import com.be.squeak_squeak.group.dto.CreateGroupRes;
import com.be.squeak_squeak.group.dto.UpdateGroupReq;
import com.be.squeak_squeak.group.dto.UpdateGroupRes;
import com.be.squeak_squeak.group.entity.UserGroup;
import com.be.squeak_squeak.group.repository.UserGroupRepository;
import com.be.squeak_squeak.groupMember.entity.GroupMember;
import com.be.squeak_squeak.groupMember.entity.MemberStatus;
import com.be.squeak_squeak.groupMember.repository.GroupMemberRepository;
import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class UserGroupService {
    private final UserGroupRepository userGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;

    public CreateGroupRes createGroup(CreateGroupReq request, Long memberId) {
        // 초대 코드 생성
         String inviteCode = generateInviteCode();


        Member creator = memberRepository.findById(memberId)
                            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        UserGroup group = UserGroup.builder()
                .name(request.name())
                .image(request.image())
                .description(request.description())
                .totalMemberCount(1) // 그룹 생성 시 1명 (그룹장)
                .inviteCode(inviteCode)
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
        SecureRandom random = new SecureRandom();
        StringBuilder inviteCode = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        for (int i = 0; i < 8; i++) { // 초대 코드는 8자리
            int index = random.nextInt(characters.length());
            inviteCode.append(characters.charAt(index));
        }

        return inviteCode.toString();
    }

    public UpdateGroupRes getUserGroup(Long groupId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        // OWNER 권한 확인
        checkOwnerPermission(group, member);

        return UpdateGroupRes.builder()
                .id(group.getId())
                .name(group.getName())
                .image(group.getImage())
                .description(group.getDescription())
                .build();
    }

    public UpdateGroupRes updateGroup(Long groupId, UpdateGroupReq request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        // OWNER 권한 확인
        checkOwnerPermission(group, member);

        group.setName(request.name());
        group.setImage(request.image());
        group.setDescription(request.description());

        userGroupRepository.save(group);

        return UpdateGroupRes.builder()
                .id(group.getId())
                .name(group.getName())
                .image(group.getImage())
                .description(group.getDescription())
                .build();
    }

    public void checkOwnerPermission(UserGroup group, Member member) {
        boolean isOwner = groupMemberRepository.findByUserGroupAndMemberAndStatus(group, member, MemberStatus.OWNER).isPresent();
        if (!isOwner) {
            throw new IllegalStateException("이 작업은 OWNER만 수행할 수 있습니다.");
        }
    }
}
