//package com.its.auth_service.service.impl;
//
//import com.its.auth_service.model.entity.RoleEntity;
//import com.its.auth_service.model.entity.RolePermissionEntity;
//import com.its.auth_service.model.request.RolePermissionRequest;
//import com.its.auth_service.model.request.RoleRequest;
//import com.its.auth_service.repository.RolePermissionRepository;
//import com.its.auth_service.repository.RoleRepository;
//import com.its.module.model.response.Response;
//import org.hamcrest.Matchers;
//import org.junit.Ignore;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//
//import javax.management.relation.Role;
//import java.util.*;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.when;
//
//class RoleServiceImplTest {
//	@Mock
//	private RoleRepository roleRepository;
//	@Mock
//	private RolePermissionRepository rolePermissionRepository;
//
//	@InjectMocks
//	RoleServiceImpl service;
//
//	@BeforeEach
//	void setUp() {
//		service = new RoleServiceImpl();
//		MockitoAnnotations.initMocks(this);
//	}
//
//
//	Integer roleId = 1;
//	Integer authId = 7;
//	Integer permissionId = 1;
//
//
//	@BeforeEach
//	void setBean() {
//
//	}
//
//	@Test
//	void getRole0() {
//		List<RoleEntity> roleEntities = new ArrayList<>();
//		when(roleRepository.findByIsActiveIsTrue()).thenReturn(roleEntities);
//		Response actualResponse = service.getAll();
//		assertThat(actualResponse, Matchers.notNullValue());
//		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
//	}
//
//	@Test
//	void addRole0() throws Exception {
//		RoleEntity roleEntity = RoleEntity.builder().build();
//		when(roleRepository.save(any(RoleEntity.class))).thenReturn(roleEntity);
//		RoleRequest roleRequest = new RoleRequest();
//		roleRequest.setName("test");
//		Response actualResult = service.addRole(roleRequest,authId);
//		assertThat(actualResult, Matchers.notNullValue());
//		assertThat(actualResult.getCode(), Matchers.equalTo(HttpStatus.CREATED.value()));
//	}
//
//
//	@Test
//	void deleteRole0() throws Exception {
//		RoleEntity roleEntity = RoleEntity.builder().id(roleId).build();
//		when(roleRepository.save(any(RoleEntity.class))).thenReturn(roleEntity);
//		when(roleRepository.findByIdAndIsActiveIsTrue(roleId)).thenReturn(roleEntity);
//
//		Response actualResult = service.deleteRole(roleId,authId);
//
//		assertThat(actualResult, Matchers.notNullValue());
//		assertThat(actualResult.getCode(), Matchers.equalTo(HttpStatus.NO_CONTENT.value()));
//	}
//
//	@Test
//	void deleteRoleThrowException1() throws Exception {
//		when(roleRepository.findByIdAndIsActiveIsTrue(roleId)).thenReturn(null);
//
//		Assertions.assertThrows(Exception.class, () -> {
//			service.deleteRole(roleId,authId);
//		});
//	}
//
//
//	@Test
//	void updateRole0() throws Exception {
//		RoleEntity roleEntity = RoleEntity.builder().id(roleId).build();
//		when(roleRepository.save(any(RoleEntity.class))).thenReturn(roleEntity);
//		when(roleRepository.findByIdAndIsActiveIsTrue(roleId)).thenReturn(roleEntity);
//		RoleRequest roleRequest = new RoleRequest();
//		roleRequest.setName("test");
//
//		Response actualResult = service.updateRole(roleId,roleRequest,authId);
//		assertThat(actualResult, Matchers.notNullValue());
//		assertThat(actualResult.getCode(), Matchers.equalTo(200));
//	}
//
//	@Test
//	void updateRoleThrowException0() throws Exception {
//		when(roleRepository.findByIdAndIsActiveIsTrue(roleId)).thenReturn(null);
//		RoleRequest roleRequest = new RoleRequest();
//		roleRequest.setName("test");
//
//		Assertions.assertThrows(Exception.class, () -> {
//			service.updateRole(roleId,roleRequest,authId);
//		});
//	}
//
//	@Test
//	void addPermission0() throws Exception {
//		RolePermissionEntity rolePermissionEntity = RolePermissionEntity.builder()
//				.roleId(roleId)
//				.permissionId(permissionId)
//				.isActive(true)
//				.build();
//
//		when(rolePermissionRepository.save(Mockito.any(RolePermissionEntity.class))).thenReturn(rolePermissionEntity);
//		when(rolePermissionRepository.findByRoleIdAndPermissionIdAndIsActiveIsTrue(eq(roleId), eq(permissionId)))
//				.thenReturn(null);
//		RolePermissionRequest rolePermissionRequest = new RolePermissionRequest();
//		rolePermissionRequest.setPermissionId(permissionId);
//		rolePermissionRequest.setRoleId(10);
//		Response actualResult = service.addPermission(rolePermissionRequest,authId);
//
//		assertThat(actualResult, Matchers.notNullValue());
//		assertThat(actualResult.getCode(), Matchers.equalTo(HttpStatus.CREATED.value()));
//	}
//
//	@Test
//	void addPermissionThrowException0() throws Exception {
//		RolePermissionEntity rolePermissionEntity;
//		List<RoleEntity> roleEntities = Arrays.asList(RoleEntity.builder().build());
//		RoleEntity roleEntity = roleEntities.get(0);
//		when(roleRepository.findByIsActiveIsTrue()).thenReturn(roleEntities);
//		when(roleRepository.save(any(RoleEntity.class))).thenReturn(roleEntity);
//		when(roleRepository.findByIdAndIsActiveIsTrue(roleId)).thenReturn(roleEntity);
//
//		rolePermissionEntity = RolePermissionEntity.builder()
//				.roleId(roleId)
//				.permissionId(permissionId)
//				.isActive(true)
//				.build();
//
//		when(rolePermissionRepository.save(Mockito.any(RolePermissionEntity.class))).thenReturn(rolePermissionEntity);
//		when(rolePermissionRepository.findByRoleIdAndPermissionIdAndIsActiveIsTrue(eq(roleId), eq(permissionId)))
//				.thenReturn(rolePermissionEntity);
//		RolePermissionRequest rolePermissionRequest = new RolePermissionRequest();
//		rolePermissionRequest.setPermissionId(permissionId);
//		rolePermissionRequest.setRoleId(roleId);
//		Assertions.assertThrows(Exception.class, () -> {
//			service.addPermission(rolePermissionRequest,authId);
//		});
//	}
//
//	@Test
//	void removePermission0() {
//		RolePermissionEntity rolePermissionEntity;
//		List<RoleEntity> roleEntities = Arrays.asList(RoleEntity.builder().build());
//		RoleEntity roleEntity = roleEntities.get(0);
//		when(roleRepository.findByIsActiveIsTrue()).thenReturn(roleEntities);
//		when(roleRepository.save(any(RoleEntity.class))).thenReturn(roleEntity);
//		when(roleRepository.findByIdAndIsActiveIsTrue(roleId)).thenReturn(roleEntity);
//
//		rolePermissionEntity = RolePermissionEntity.builder()
//				.roleId(roleId)
//				.permissionId(permissionId)
//				.isActive(true)
//				.build();
//
//		when(rolePermissionRepository.save(Mockito.any(RolePermissionEntity.class))).thenReturn(rolePermissionEntity);
//		when(rolePermissionRepository.findByRoleIdAndPermissionIdAndIsActiveIsTrue(eq(roleId), eq(permissionId)))
//				.thenReturn(rolePermissionEntity);
//		RolePermissionRequest rolePermissionRequest = new RolePermissionRequest();
//		rolePermissionRequest.setPermissionId(permissionId);
//		rolePermissionRequest.setRoleId(roleId);
//
//		Response actualResult = service.removePermission(rolePermissionRequest, authId);
//		assertThat(actualResult, Matchers.notNullValue());
//		assertThat(actualResult.getCode(), Matchers.equalTo(HttpStatus.NO_CONTENT.value()));
//	}
//
//	@Test
//	void removePermissionThrowException0() {
//		when(rolePermissionRepository.findByRoleIdAndPermissionIdAndIsActiveIsTrue(eq(roleId), eq(permissionId)))
//				.thenReturn(null);
//		RolePermissionRequest rolePermissionRequest = new RolePermissionRequest();
//		rolePermissionRequest.setPermissionId(permissionId);
//		rolePermissionRequest.setRoleId(10);
//
//		Assertions.assertThrows(Exception.class, () -> service.removePermission(rolePermissionRequest, authId));
//	}
//}