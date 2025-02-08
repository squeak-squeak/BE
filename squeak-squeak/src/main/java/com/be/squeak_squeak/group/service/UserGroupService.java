package com.be.squeak_squeak.group.service;

import com.be.squeak_squeak.common.auth.MemberInfo;
import com.be.squeak_squeak.group.dto.CreateGroupReq;
import com.be.squeak_squeak.group.dto.CreateGroupRes;
import com.be.squeak_squeak.group.dto.GetGoupMemberListRes;
import com.be.squeak_squeak.group.dto.GetGroupMemberRes;
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

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserGroupService {
    private final UserGroupRepository userGroupRepository;
    private final GroupMemberRepository groupMemberRepository;
    private final MemberRepository memberRepository;
    private final UserGroupCustomRepository userGroupCustomRepository;

    @Value("${file}")
    private String rootFilePath;

    @Transactional(readOnly = true)
    public GetGoupMemberListRes getGroupUsers(Long groupId, MemberInfo memberInfo) {
        Member member = memberRepository.findById(memberInfo.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        // 그룹 멤버 조회 (OWNER, MEMBER)
        List<GetGroupMemberRes> memberResList = groupMemberRepository.findGroupUsersByGroupIdAndStatus(group.getId(),
                List.of(MemberStatus.OWNER, MemberStatus.MEMBER));
        // 가입한 사용자 정보 조회
        GroupMember groupMember = groupMemberRepository.findByUserGroupAndMember(group, member)
                .orElseThrow(() -> new IllegalArgumentException("그룹에 가입되어 있지 않습니다."));
        // 로그인한 사용자의 권한 조회
        MemberStatus logedInMemberStatus = groupMember.getStatus();
        if(logedInMemberStatus == MemberStatus.WAITING) {
            throw new IllegalStateException("가입 승인 대기중인 사용자입니다.");
        }
        // 대기중인 사용자 수 조회
        List<GroupMember> waitingMembers = groupMemberRepository.findByUserGroupAndStatus(group, MemberStatus.WAITING);
        int waitingCount = waitingMembers.size();

        return new GetGoupMemberListRes(
                logedInMemberStatus == MemberStatus.OWNER,
                waitingCount,
                memberResList
        );
    }

    @Transactional
    public CreateGroupRes createGroup(MemberInfo memberInfo, CreateGroupReq request) {
        Member member = memberRepository.findById(memberInfo.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 초대 코드 생성
        String inviteCode = generateInviteCode();

        UserGroup group = UserGroup.builder()
                .name(request.name())
                .image(null)
                .description(request.description())
                .totalMemberCount(1) // 그룹 생성 시 1명 (그룹장)
                .inviteCode(inviteCode)
                .build();

//        saveGroupImage(file, group);

        // 그룹 저장
        userGroupRepository.save(group);

        GroupMember groupMember = GroupMember.builder()
                .userGroup(group)
                .status(MemberStatus.OWNER)
                .member(member)
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
    public UpdateGroupRes getUserGroup(MemberInfo memberInfo, Long groupId) {
        Member member = memberRepository.findById(memberInfo.getId())
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

    @Transactional(readOnly = true)
    public Resource getGroupImage(MemberInfo memberInfo, Long groupId) {
        Member member = memberRepository.findById(memberInfo.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        // Owner만 이미지 조회 가능하도록 처리 가능
        checkOwnerPermission(group, member);

        if (!StringUtils.hasText(group.getImage())) {
            throw new IllegalArgumentException("이미지가 존재하지 않습니다.");
        }

        try {
            Path imagePath = Paths.get(rootFilePath, group.getImage()).toAbsolutePath();
            Resource imageResource = new UrlResource(imagePath.toUri());

            if (imageResource.exists() && imageResource.isReadable()) {
                return imageResource;
            } else {
                throw new IllegalArgumentException("이미지를 읽을 수 없습니다.");
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("이미지 경로가 잘못되었습니다.");
        }
    }

    @Transactional
    public UpdateGroupRes updateGroup(MemberInfo memberInfo, Long groupId, UpdateGroupReq request) {
        Member member = memberRepository.findById(memberInfo.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        // OWNER 권한 확인
        checkOwnerPermission(group, member);

        group.updateGroup(request.name(), request.description());
//        saveGroupImage(file, group);

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
    public void saveGroupImage(MemberInfo memberInfo, Long groupId, MultipartFile file) {
        Member member = memberRepository.findById(memberInfo.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        checkOwnerPermission(group, member);

        try {
            if (StringUtils.hasText(group.getImage())) {
                Path oldPath = Paths.get(rootFilePath, group.getImage()).toAbsolutePath();
                Files.deleteIfExists(oldPath);
            }

            String newImageName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path newPath = Paths.get(rootFilePath, newImageName).toAbsolutePath();
            Files.createDirectories(newPath.getParent());
            file.transferTo(newPath.toFile());

            group.updateGroupImage(newImageName);
            userGroupRepository.save(group);

        } catch (IOException e) {
            throw new IllegalArgumentException("이미지 저장 중 오류가 발생했습니다.");
        }
    }

//    @Transactional
//    public void saveGroupImage(MultipartFile file, UserGroup group) {
//
//        try {
//            // 기존 이미지 삭제 (있다면)
//            if (StringUtils.hasText(group.getImage())) {
//                Path oldPath = Paths.get(rootFilePath, group.getImage()).toAbsolutePath();
//                Files.deleteIfExists(oldPath);
//            }
//
//            // 새 이미지 저장
//            String newImageName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//            Path newPath = Paths.get(rootFilePath, newImageName).toAbsolutePath();
//            Files.createDirectories(newPath.getParent());
//            file.transferTo(newPath.toFile());
//
//            // 그룹 이미지 업데이트
//            group.updateGroupImage(newImageName);
////            userGroupRepository.save(group);
//
//        } catch (IOException e) {
//            throw new IllegalArgumentException("이미지 저장 중 오류가 발생했습니다.");
//        }
//    }

    @Transactional
    public void requestJoinGroup(MemberInfo memberInfo, JoinGroupReq joinGroupReq) {
        Member member = memberRepository.findById(memberInfo.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

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

