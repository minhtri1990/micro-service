package com.its.auth_service.controller;

import com.its.auth_service.model.request.*;
import com.its.module.model.response.BaseResponse;
import com.its.module.utils.ObjectUtils;
import com.its.module.utils.PermissionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.its.auth_service.service.UserService;
import com.its.module.model.exception.BadRequestException;
import com.its.module.model.exception.ForbiddenException;
import com.its.module.model.exception.UnauthorizedException;
import com.its.module.utils.Constants.AuthHeader;
import com.its.module.utils.Constants.Pagination;
import com.its.module.utils.Constants.UserRole;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@GetMapping
	public BaseResponse getAllUser(@RequestParam Map<String, String> params,
			@RequestParam(value = Pagination.PAGE_REQUEST_PARAM, defaultValue = Pagination.PAGE_DEFAULT) Integer offset,
			@RequestParam(value = Pagination.PAGE_SIZE_REQUEST_PARAM, defaultValue = Pagination.PAGE_SIZE_DEFAULT) Integer limit) {
		return userService.getAllUser(params, offset, limit);
	}

	@RequestMapping(value = "/extract", method = RequestMethod.GET)
	public ResponseEntity getAllUserExcel(@RequestParam Map<String, String> params,
										  @RequestParam(value = Pagination.PAGE_REQUEST_PARAM, defaultValue = Pagination.PAGE_DEFAULT) Integer offset,
										  @RequestParam(value = Pagination.PAGE_SIZE_REQUEST_PARAM, defaultValue = Pagination.PAGE_SIZE_DEFAULT) Integer limit) {
		return userService.extractUser(params, offset, limit);
	}

	@PostMapping
	public BaseResponse createUser(@RequestBody @Valid UserWithoutPasswordCreateRequest userCreateRequest,
								   @RequestHeader(AuthHeader.USER_ID) Integer authId) {
		return userService.createUser(userCreateRequest, authId);
	}

	/**
	 * sysadmin role=0 can access any account info userId=0 to get self-info
	 * 
	 * @param userId
	 * @param authId
	 * @return
	 */

	@GetMapping("/{user_id}")
	@PreAuthorize("#userId == 0 or #authId.equals(#userId) or hasAnyAuthority('0','2')")
	public BaseResponse getUserInfo(@PathVariable("user_id") Integer userId,
							  @RequestHeader(AuthHeader.USER_ID) Integer authId) {
		if(userId == 0)
			userId = authId;
		return userService.getUserInfo(userId);
	}

	/**
	 * user can only change self-info
	 * 
	 * @param userId
	 * @param authId
	 * @param userInfoRequest
	 * @return
	 */
	@PutMapping("/{user_id}")
	@PreAuthorize("#userId == 0 or #authId.equals(#userId) or hasAnyAuthority('0','4')")
	public BaseResponse updateUserInfo(@PathVariable("user_id") Integer userId,
								 		@RequestHeader(AuthHeader.USER_ID) Integer authId,
								 		@RequestBody @Valid UserInfoRequest userInfoRequest) {
		if(userId == 0)
			userId = authId;
		return userService.updateUserInfo(userId, userInfoRequest, authId);
	}

	@DeleteMapping("/{user_id}")
	@PreAuthorize("hasAnyAuthority('0','4')")
	public Object deleteUser(@PathVariable("user_id") Integer userId,
								 @RequestHeader(AuthHeader.USER_ID) Integer authId) {
		return userService.deleteUser(userId, authId);
	}

	/**
	 * only sysadmin role
	 * 
	 * @param userId
	 * @param userRoleRequest
	 * @return
	 */
	@PutMapping("/{user_id}/roles")
	public Object updateUserRole(@PathVariable("user_id") Integer userId,
								 @RequestBody @Valid UserRoleRequest userRoleRequest,
								 Authentication authentication) {
		return userService.changeUserRole(userId, userRoleRequest, (Integer) authentication.getPrincipal());
	}

	@PutMapping("/{user_id}/admin")
	public Object updateUserAdminRole(@PathVariable("user_id") Integer userId,
								 @RequestBody @Valid UserInfoAdminRequest userInfoAdminRequest,
								 Authentication authentication) {
		return userService.updateUserInfoAdminRole(userId, userInfoAdminRequest, (Integer) authentication.getPrincipal());
	}

	@PutMapping("/{user_id}/admin_password")
	public Object updatePasswordAdminRole(@PathVariable("user_id") Integer userId,
								 Authentication authentication) {
		return userService.resetPasswordAdmin(userId, (Integer) authentication.getPrincipal());
	}

	@RequestMapping(value = "/avatars", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public BaseResponse uploadAvatar(@RequestParam("files") MultipartFile file,
			@RequestHeader(AuthHeader.USER_ID) Integer authId) {
		return userService.uploadFile(file, authId);
	}

	@PutMapping("/change_password")
	public BaseResponse changePassword(@RequestHeader(AuthHeader.USERNAME) String username,
									   @RequestBody @Valid ChangePasswordRequest changePasswordRequest) {
		return userService.changePassword(username, changePasswordRequest);
	}

	@PostMapping("/forgot_password")
	public Object forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) {
		return userService.forgotPassword(forgotPasswordRequest);
	}

	@PostMapping("/resend_forgot_password")
	public Object resend_forgot_password(@RequestBody @Valid ResentForgotPasswordRequest resentForgotPasswordRequest) {
		return userService.resendForgotPassword(resentForgotPasswordRequest);
	}

	@PostMapping("/reset_password")
	public Object resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
		return userService.resetPassword(resetPasswordRequest);
	}
}
