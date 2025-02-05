package com.be.squeak_squeak.groupMember.controller;

import com.be.squeak_squeak.common.auth.AuthMember;
import com.be.squeak_squeak.common.auth.MemberInfo;
import com.be.squeak_squeak.common.config.ApiResponse;
import com.be.squeak_squeak.common.config.ApiResponseGenerator;
import com.be.squeak_squeak.groupMember.dto.RequestListRes;
import com.be.squeak_squeak.groupMember.service.GroupMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group-member")
public class GroupMemberController {
    private final GroupMemberService groupMemberService;

    @GetMapping("/{groupId}/join-requests")
    public ApiResponse<ApiResponse.CustomBody<List<RequestListRes>>> getRequestList(@PathVariable Long groupId,
                                                                                    @AuthMember MemberInfo memberInfo){
        List<RequestListRes> requestList = groupMemberService.getRequestList(groupId, memberInfo);
        return ApiResponseGenerator.success(requestList, HttpStatus.OK);
    }
}
