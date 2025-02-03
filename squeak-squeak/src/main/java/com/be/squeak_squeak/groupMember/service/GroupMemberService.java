package com.be.squeak_squeak.groupMember.service;

import com.be.squeak_squeak.common.auth.MemberInfo;
import com.be.squeak_squeak.groupMember.dto.RequestListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupMemberService {
    @Transactional(readOnly = true)
    public List<RequestListRes> getRequestList(Long groupId, MemberInfo memberInfo) {
        return null;
    }
}
