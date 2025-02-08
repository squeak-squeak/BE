package com.be.squeak_squeak.groupMember.repository;

import com.be.squeak_squeak.group.dto.GetGroupMemberRes;
import com.be.squeak_squeak.group.entity.UserGroup;
import com.be.squeak_squeak.groupMember.entity.GroupMember;
import com.be.squeak_squeak.groupMember.entity.MemberStatus;
import com.be.squeak_squeak.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    Optional<GroupMember> findByUserGroupAndMemberAndStatus(UserGroup userGroup, Member member, MemberStatus status);

    List<GroupMember> findByUserGroupAndStatus(UserGroup group, MemberStatus memberStatus);

    @Query("SELECT new com.be.squeak_squeak.group.dto.GetGroupMemberRes(gm.member.id, gm.member.nickname, gm.member.image) "
            +
            "FROM GroupMember gm " +
            "WHERE gm.userGroup.id = :groupId " +
            "AND gm.status IN (:statuses)")
    List<GetGroupMemberRes> findGroupUsersByGroupIdAndStatus(@Param("groupId") Long groupId,
                                                             @Param("statuses") List<MemberStatus> statuses);

    Optional<GroupMember> findByUserGroupAndMember(UserGroup userGroup, Member member);
}
