package com.be.squeak_squeak.groupMember.service;

import com.be.squeak_squeak.common.auth.MemberInfo;
import com.be.squeak_squeak.group.entity.UserGroup;
import com.be.squeak_squeak.group.repository.UserGroupRepository;
import com.be.squeak_squeak.groupMember.dto.RequestListRes;
import com.be.squeak_squeak.groupMember.entity.GroupMember;
import com.be.squeak_squeak.groupMember.entity.MemberStatus;
import com.be.squeak_squeak.groupMember.repository.GroupMemberRepository;
import com.be.squeak_squeak.member.entity.Member;
import com.be.squeak_squeak.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberService {
    private final UserGroupRepository userGroupRepository;
    private final MemberRepository memberRepository;
    private final GroupMemberRepository groupMemberRepository;

    @Transactional(readOnly = true)
    public List<RequestListRes> getRequestList(Long groupId, MemberInfo memberInfo) {
        Member member = memberRepository.findById(memberInfo.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        UserGroup group = userGroupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 그룹입니다."));

        // 특정 그룹에서 OWNER인지 확인
        groupMemberRepository.findByUserGroupAndMemberAndStatus(group, member, MemberStatus.OWNER)
                .orElseThrow(() -> new IllegalArgumentException("회원은 그룹의 OWNER가 아닙니다."));

        // "waiting" 상태인 멤버 조회
        List<GroupMember> waitingMembers = groupMemberRepository.findByUserGroupAndStatus(group, MemberStatus.WAITING);

        return waitingMembers.stream()
                .map(g -> new RequestListRes(g.getMember().getId(), g.getMember().getNickname(), group.getName()))
                .toList();
    }
}
