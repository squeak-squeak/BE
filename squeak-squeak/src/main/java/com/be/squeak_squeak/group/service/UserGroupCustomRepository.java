package com.be.squeak_squeak.group.service;

import static com.be.squeak_squeak.group.entity.QUserGroup.userGroup;
import static com.be.squeak_squeak.groupMember.entity.QGroupMember.groupMember;

import com.be.squeak_squeak.group.entity.UserGroup;
import com.be.squeak_squeak.groupMember.entity.MemberStatus;
import com.be.squeak_squeak.groupMember.entity.QGroupMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserGroupCustomRepository {

    private final JPAQueryFactory queryFactory;

    public List<UserGroup> searchGroup(String type, String keyword, Long memberId) {
        return queryFactory.select(userGroup)
                .from(userGroup)
                .join(userGroup.groupMembers, QGroupMember.groupMember)
                .where(keywordEq(keyword), typeEq(type, memberId))
                .fetch();
    }


    private BooleanExpression keywordEq(String keyword) {
        return !keyword.isEmpty() ? userGroup.name.contains(keyword) : null;
    }

    private BooleanExpression typeEq(String type, Long memberId) {
        return type.equals("OWNER") ? groupMember.status.eq(MemberStatus.valueOf("OWNER"))
                .and(groupMember.member.id.eq(memberId)) : null;
    }


}
