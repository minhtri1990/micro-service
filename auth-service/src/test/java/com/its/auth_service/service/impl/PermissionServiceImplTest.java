//package com.its.auth_service.service.impl;
//
//import com.its.auth_service.model.entity.PermissionEntity;
//import com.its.auth_service.model.request.PermissionRequest;
//import com.its.auth_service.repository.PermissionRepository;
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
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.mockito.Mockito.when;
//
//class PermissionServiceImplTest {
//
//	@Mock
//	private PermissionRepository permissionRepository;
//
//	@InjectMocks
//	PermissionServiceImpl service;
//
//	@BeforeEach
//	void setUp() {
//		MockitoAnnotations.initMocks(this);
//	}
//
//
//	@Test
//	void getAll0() {
//		when(permissionRepository.findAll()).thenReturn(Arrays.asList(new PermissionEntity()));
//		Response actualResponse = service.getAll();
//		assertThat(actualResponse, Matchers.notNullValue());
//		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
//	}
//}