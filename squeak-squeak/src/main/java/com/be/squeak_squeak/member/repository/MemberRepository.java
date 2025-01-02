package com.be.squeak_squeak.member.repository;


import com.be.squeak_squeak.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findBySocialUuid(String socialUuid);

}
