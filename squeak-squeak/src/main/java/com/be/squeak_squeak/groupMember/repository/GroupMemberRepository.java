package com.be.squeak_squeak.groupMember.repository;

import com.be.squeak_squeak.group.entity.UserGroup;
import com.be.squeak_squeak.groupMember.entity.GroupMember;
import com.be.squeak_squeak.groupMember.entity.MemberStatus;
import com.be.squeak_squeak.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByUserGroupAndMemberAndStatus(UserGroup userGroup, Member member, MemberStatus status);
}
