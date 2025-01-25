package com.be.squeak_squeak.group.repository;

import com.be.squeak_squeak.group.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
}
