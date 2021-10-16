package com.smartcity.its.manager.violation.service.impl;

import com.its.module.model.response.BaseResponse;
import com.smartcity.its.manager.violation.model.entity.WarningConfigEntity;
import com.smartcity.its.manager.violation.model.request.warning.WarningConfigRequest;
import com.smartcity.its.manager.violation.repository.ViolationTypeRepository;
import com.smartcity.its.manager.violation.repository.WarningConfigItemRepository;
import com.smartcity.its.manager.violation.repository.WarningConfigRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

class WarningConfigServiceImplTest {

	@Mock
	private WarningConfigRepository warningConfigRepository;
	@Mock
	private WarningConfigItemRepository warningConfigItemRepository;
	@Mock
	private ViolationTypeRepository violationTypeRepository;
	@Mock
	private ModelMapper mapper;
	@InjectMocks 
	WarningConfigServiceImpl service;

	@BeforeEach
	void setUp() {
		service = new WarningConfigServiceImpl();
		MockitoAnnotations.initMocks(this);
	}

	//@Test
	void getByUserIdAndType1() throws Exception {
		//TODO: sua dau vao phu hop voi nghiep vu
		Integer userId = 0;
		Integer type = 0;

		// invoke method
		BaseResponse actualResult = service.getByUserIdAndType(userId,type);

		//TODO: thay doi assert result phu hop voi nghiep vu
		assertThat(actualResult, Matchers.notNullValue());
	}

	@Test
	void getByUserIdAndType1ThrowException2() throws Exception {
		Integer userId = 0;
		Integer type = 0;
		Assertions.assertThrows(Exception.class, () -> {
			service.getByUserIdAndType(userId,type);
		});
	}

	//@Test
	void update0() throws Exception {
		Integer userId = 0;
		WarningConfigRequest warningConfigRequest = new WarningConfigRequest();
		
		WarningConfigEntity resultData1 = new WarningConfigEntity();
		when(warningConfigRepository.save(Mockito.any())).thenReturn(resultData1);
		List resultData3 = new ArrayList<>();
		when(warningConfigItemRepository.findByUserIdAndTypeAndIsActiveIsTrue(Mockito.any(),Mockito.any())).thenReturn(resultData3);
		List resultData4 = new ArrayList<>();
		when(violationTypeRepository.findByTypeAndIsActiveIsTrue(Mockito.any())).thenReturn(resultData4);
		List resultData5 = new ArrayList<>();
		when(warningConfigItemRepository.saveAll(Mockito.any())).thenReturn(resultData5);
		List resultData6 = new ArrayList<>();
		when(warningConfigItemRepository.saveAll(Mockito.any())).thenReturn(resultData6);
		
		Authentication auth = new UsernamePasswordAuthenticationToken(16, "123");
		
		BaseResponse actualResult = service.update(userId,warningConfigRequest,auth);

		assertThat(actualResult, Matchers.notNullValue());
	}

	@Test
	void update0ThrowException2() throws Exception {
		Integer userId = 0;
		WarningConfigRequest warningConfigRequest = new WarningConfigRequest();
	
		Authentication auth = new UsernamePasswordAuthenticationToken(16, "123");
		Assertions.assertThrows(Exception.class, () -> {
			service.update(userId,warningConfigRequest,auth);
		});
	}

}