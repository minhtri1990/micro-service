package com.its.auth_service.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.its.auth_service.model.request.*;
import com.its.module.model.response.Response;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public interface UserService {
	Response<?> getAllUser(Map<String, String> params, Integer offset, Integer limit);

	ResponseEntity<?> extractUser(Map<String, String> params, Integer offset, Integer limit);

	Response<?> createUser(UserWithoutPasswordCreateRequest userCreateRequest, Integer authId);

	Response<?> getUserInfo(Integer userId);

	Response<?> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);

	Response<?> resendForgotPassword(ResentForgotPasswordRequest resentForgotPasswordRequest);

	Response<?> resetPassword(ResetPasswordRequest resetPasswordRequest);

	Response<?> updateUserInfo(Integer userId, UserInfoRequest userInfoRequest, Integer authId);

	Response<?> updateUserInfoAdminRole(Integer userId, UserInfoAdminRequest userInfoAdminRequest, Integer authId);

	Response<?> changePassword(String userName, ChangePasswordRequest changePasswordRequest);

	Response<?> changeUserRole(Integer userId, UserRoleRequest userRoleRequest, Integer authId);

	Response<?> resetPasswordAdmin(Integer userId, Integer authId);

	Response<?> uploadFile(MultipartFile file, Integer authId);

	Response<?> deleteUser(Integer userId, Integer authId);
}
