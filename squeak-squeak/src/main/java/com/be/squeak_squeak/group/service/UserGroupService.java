package com.be.squeak_squeak.group.service;

import com.be.squeak_squeak.common.auth.MemberInfo;
import com.be.squeak_squeak.group.dto.CreateGroupReq;
import com.be.squeak_squeak.group.dto.CreateGroupRes;
import com.be.squeak_squeak.group.dto.JoinGroupReq;
import com.be.squeak_squeak.group.dto.SearchGroupRes;
import com.be.squeak_squeak.group.dto.UpdateGroupReq;
import com.be.squeak_squeak.group.dto.UpdateGroupRes;
import com.be.squeak_squeak.group.entity.UserGroup;
import com.be.squeak_squeak.group.repository.UserGroupRepository;
import com.be.squeak_squeak.groupMember.entity.GroupMember;
import com.be.squeak_squeak.groupMember.entity.MemberStatus;
import com.be.squeak_squeak.groupMember.repository.GroupMemberRepository;
import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserGroupService {
    private final UserGroupRepository userGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;
    private final UserGroupCustomRepository userGroupCustomRepository;

    @Transactional
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

    @Transactional(readOnly = true)
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

    @Transactional
    public UpdateGroupRes updateGroup(Long groupId, UpdateGroupReq request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        // OWNER 권한 확인
        checkOwnerPermission(group, member);

        group.updateGroup(request.name(), request.image(), request.description());

        return UpdateGroupRes.builder()
                .id(group.getId())
                .name(group.getName())
                .image(group.getImage())
                .description(group.getDescription())
                .build();
    }

    @Transactional(readOnly = true)
    public List<SearchGroupRes> searchGroup(MemberInfo memberInfo, String type, String keyword) {
        Member member = memberRepository.findById(memberInfo.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        List<UserGroup> userGroupList = userGroupCustomRepository.searchGroup(type, keyword, member.getId());

        return userGroupList.stream()
                .map(group -> SearchGroupRes.builder()
                        .id(group.getId())
                        .image(group.getImage())
                        .name(group.getName())
                        .build())
                .toList();
    }

    @Transactional
    public void requestJoinGroup(MemberInfo memberInfo, JoinGroupReq joinGroupReq) {
        Member member = memberRepository.findById(memberInfo.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        System.out.println("joinGroupReq.inviteCode() = " + joinGroupReq.inviteCode());
        UserGroup group = userGroupRepository.findByInviteCode(joinGroupReq.inviteCode()).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 그룹입니다.")
        );

        GroupMember groupMember = GroupMember.builder()
                .userGroup(group)
                .member(member)
                .status(MemberStatus.WAITING)
                .build();

        groupMemberRepository.save(groupMember);
    }

    public void checkOwnerPermission(UserGroup group, Member member) {
        boolean isOwner = groupMemberRepository.findByUserGroupAndMemberAndStatus(group, member, MemberStatus.OWNER)
                .isPresent();
        if (!isOwner) {
            throw new IllegalStateException("이 작업은 OWNER만 수행할 수 있습니다.");
        }
    }


}

