package com.be.squeak_squeak.groupMember.repository;

import com.be.squeak_squeak.groupMember.entity.GroupMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
}
