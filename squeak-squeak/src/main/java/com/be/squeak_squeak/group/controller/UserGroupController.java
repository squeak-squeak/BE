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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ApiResponse<ApiResponse.CustomBody<CreateGroupRes>> createGroup(@AuthMember MemberInfo memberInfo,
                                                                            @RequestBody CreateGroupReq request) {
        CreateGroupRes createGroupRes = userGroupService.createGroup(memberInfo, request);
        return ApiResponseGenerator.success(createGroupRes, HttpStatus.OK);
    }

    // 그룹 이미지 생성
    @PostMapping(value ="/create/{groupId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ApiResponse.CustomBody<Void>> createImageGroup(@AuthMember MemberInfo memberInfo,
                                                                      @PathVariable Long groupId,
                                                                      @RequestParam MultipartFile file) {
        userGroupService.saveGroupImage(memberInfo, groupId, file);
        return ApiResponseGenerator.success(null, HttpStatus.OK);
    }

    // 그룹 조회
    @GetMapping("/read/{groupId}")
    public ApiResponse<ApiResponse.CustomBody<UpdateGroupRes>> getUserGroup(@AuthMember MemberInfo memberInfo,
                                                                            @PathVariable Long groupId) {
        UpdateGroupRes response = userGroupService.getUserGroup(memberInfo, groupId);
        return ApiResponseGenerator.success(response, HttpStatus.OK);
    }

    // 그룹 이미지 조회
    @GetMapping(value = "/read/{groupId}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getGroupImage(@AuthMember MemberInfo memberInfo,
                                                  @PathVariable Long groupId) {

        Resource imageResource = userGroupService.getGroupImage(memberInfo, groupId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageResource.getFilename() + "\"")
                .body(imageResource);
    }

    // 그룹 수정
    @PutMapping("/update/{groupId}")
    public ApiResponse<ApiResponse.CustomBody<UpdateGroupRes>> updateGroup(@AuthMember MemberInfo memberInfo,
                                                                            @PathVariable Long groupId,
                                                                           @RequestBody UpdateGroupReq request) {
        UpdateGroupRes response = userGroupService.updateGroup(memberInfo, groupId, request);
        return ApiResponseGenerator.success(response, HttpStatus.OK);
    }

    // 그룹 이미지 수정
    @PutMapping(value = "/update/{groupId}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<ApiResponse.CustomBody<Void>> updateImageGroup(@AuthMember MemberInfo memberInfo,
                                                                           @PathVariable Long groupId,
                                                                           @RequestParam MultipartFile file) {
        userGroupService.saveGroupImage(memberInfo, groupId, file);
        return ApiResponseGenerator.success(null, HttpStatus.OK);
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
