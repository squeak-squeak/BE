package com.be.squeak_squeak.group.controller;

import com.be.squeak_squeak.common.config.ApiResponse;
import com.be.squeak_squeak.common.config.ApiResponseGenerator;
import com.be.squeak_squeak.group.dto.CreateGroupReq;
import com.be.squeak_squeak.group.dto.CreateGroupRes;
import com.be.squeak_squeak.group.dto.UpdateGroupReq;
import com.be.squeak_squeak.group.dto.UpdateGroupRes;
import com.be.squeak_squeak.group.service.UserGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/groups")
public class UserGroupController {
    private final UserGroupService userGroupService;

    @PostMapping("/create")
    public ApiResponse<ApiResponse.CustomBody<CreateGroupRes>> createGroup(@RequestBody CreateGroupReq request,
                                                                           @RequestParam Long memberId) {
        CreateGroupRes createGroupRes = userGroupService.createGroup(request, memberId);
        return ApiResponseGenerator.success(createGroupRes, HttpStatus.OK);
    }

    @GetMapping("/update/{groupId}")
    public ApiResponse<ApiResponse.CustomBody<UpdateGroupRes>> getUserGroup(@PathVariable Long groupId,
                                                                            @RequestBody UpdateGroupReq request,
                                                                            @RequestParam Long memberId){
        UpdateGroupRes response = userGroupService.getUserGroup(groupId, request, memberId);
        return ApiResponseGenerator.success(response, HttpStatus.OK);
    }

    @PutMapping("/update/{groupId}")
    public ApiResponse<ApiResponse.CustomBody<UpdateGroupRes>> updateGroup(@PathVariable Long groupId,
                                                                           @RequestBody UpdateGroupReq request,
                                                                           @RequestParam Long memberId){
        UpdateGroupRes response = userGroupService.updateGroup(groupId, request, memberId);
        return ApiResponseGenerator.success(response, HttpStatus.OK);
    }
}
