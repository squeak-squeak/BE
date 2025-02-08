package com.be.squeak_squeak.group.controller;

import com.be.squeak_squeak.common.auth.AuthMember;
import com.be.squeak_squeak.common.auth.MemberInfo;
import com.be.squeak_squeak.common.config.ApiResponse;
import com.be.squeak_squeak.common.config.ApiResponse.CustomBody;
import com.be.squeak_squeak.common.config.ApiResponseGenerator;
import com.be.squeak_squeak.group.dto.CreateGroupReq;
import com.be.squeak_squeak.group.dto.CreateGroupRes;
import com.be.squeak_squeak.group.dto.GetGoupMemberListRes;
import com.be.squeak_squeak.group.dto.GetGroupMemberRes;
import com.be.squeak_squeak.group.dto.JoinGroupReq;
import com.be.squeak_squeak.group.dto.SearchGroupRes;
import com.be.squeak_squeak.group.dto.UpdateGroupReq;
import com.be.squeak_squeak.group.dto.UpdateGroupRes;
import com.be.squeak_squeak.group.service.UserGroupService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class UserGroupController {
    private final UserGroupService userGroupService;

    @GetMapping("/{groupId}")
    public ApiResponse<ApiResponse.CustomBody<GetGoupMemberListRes>> getGroupUsers(@PathVariable Long groupId,
                                                                                   @AuthMember MemberInfo memberInfo) {
        GetGoupMemberListRes response = userGroupService.getGroupUsers(groupId, memberInfo);
        return ApiResponseGenerator.success(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ApiResponse<ApiResponse.CustomBody<CreateGroupRes>> createGroup(@RequestBody CreateGroupReq request,
                                                                           @RequestParam Long memberId) {
        CreateGroupRes createGroupRes = userGroupService.createGroup(request, memberId);
        return ApiResponseGenerator.success(createGroupRes, HttpStatus.OK);
    }

    @GetMapping("/update/{groupId}")
    public ApiResponse<ApiResponse.CustomBody<UpdateGroupRes>> getUserGroup(@PathVariable Long groupId,
                                                                            @RequestParam Long memberId) {
        UpdateGroupRes response = userGroupService.getUserGroup(groupId, memberId);
        return ApiResponseGenerator.success(response, HttpStatus.OK);
    }

    @PutMapping("/update/{groupId}")
    public ApiResponse<ApiResponse.CustomBody<UpdateGroupRes>> updateGroup(@PathVariable Long groupId,
                                                                           @RequestBody UpdateGroupReq request,
                                                                           @RequestParam Long memberId) {
        UpdateGroupRes response = userGroupService.updateGroup(groupId, request, memberId);
        return ApiResponseGenerator.success(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ApiResponse<CustomBody<List<SearchGroupRes>>> searchGroup(@AuthMember MemberInfo memberInfo,
                                                                     @RequestParam(value = "type") String type,
                                                                     @RequestParam(value = "keyword", required = false) String keyword) {
        List<SearchGroupRes> response = userGroupService.searchGroup(memberInfo, type, keyword);
        return ApiResponseGenerator.success(response, HttpStatus.OK);
    }

    @PostMapping("/request")
    public ApiResponse<CustomBody<Void>> requestJoinGroup(@AuthMember MemberInfo memberInfo,
                                                          @RequestBody JoinGroupReq request) {
        userGroupService.requestJoinGroup(memberInfo, request);
        return ApiResponseGenerator.success(HttpStatus.OK);
    }

}
