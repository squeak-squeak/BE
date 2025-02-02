package com.be.squeak_squeak.group.repository;

import com.be.squeak_squeak.group.entity.UserGroup;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    @Query("SELECT g FROM UserGroup g WHERE g.inviteCode = :inviteCode")
    Optional<UserGroup> findByInviteCode(String inviteCode);
}
