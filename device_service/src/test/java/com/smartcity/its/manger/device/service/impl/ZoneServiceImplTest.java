package com.smartcity.its.manger.device.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.its.module.model.entity.DeviceEntity;
import com.its.module.model.entity.ViolationTypeEntity;
import com.its.module.model.response.BaseResponse;
import com.its.module.model.response.Response;
import com.smartcity.its.manager.device.model.dto.zone.ZoneAttrRequest;
import com.smartcity.its.manager.device.model.dto.zone.ZoneCreateRequest;
import com.smartcity.its.manager.device.model.dto.zone.ZoneInfoRequest;
import com.smartcity.its.manager.device.model.dto.zone.ZonePointRequest;
import com.smartcity.its.manager.device.model.entity.ZoneAttrEntity;
import com.smartcity.its.manager.device.model.entity.ZoneEntity;
import com.smartcity.its.manager.device.model.entity.ZoneTypeEntity;
import com.smartcity.its.manager.device.repository.DeviceRepository;
import com.smartcity.its.manager.device.repository.ScpFeignClient;
import com.smartcity.its.manager.device.repository.ViolationTypeRepository;
import com.smartcity.its.manager.device.repository.ZoneAttrRepository;
import com.smartcity.its.manager.device.repository.ZoneRepository;
import com.smartcity.its.manager.device.repository.ZoneTypeRepository;
import com.smartcity.its.manager.device.service.impl.ZoneServiceImpl;

class ZoneServiceImplTest {

	@Mock
	private ZoneRepository zoneRepository;
	@Mock
	private DeviceRepository deviceRepository;
	@Mock
	private ScpFeignClient scpFeignClient;
	@Mock
	private ZoneTypeRepository zoneTypeRepository;
	@Mock
	private ViolationTypeRepository violationTypeRepository;
	@Mock
	private ZoneAttrRepository zoneAttrRepository;
	@InjectMocks 
	ZoneServiceImpl service;
	
	Authentication authentication = new UsernamePasswordAuthenticationToken(16, "Token");

	@BeforeEach
	void setUp() {
		service = new ZoneServiceImpl();
		MockitoAnnotations.initMocks(this);
	}


	@Test
	void createNew5ThrowException1() throws Exception {
		ZoneCreateRequest zoneCreateRequest = new ZoneCreateRequest();
		
		Assertions.assertThrows(Exception.class, () -> {
			service.createNew(zoneCreateRequest, authentication);
		});
	}

	@Test
	void createNew() {
		when(zoneRepository.save(any(ZoneEntity.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
		ZoneTypeEntity zoneType = ZoneTypeEntity.builder().id(1).violationTypeId(1).build();
		when(zoneTypeRepository.findById(any())).thenReturn(Optional.of(zoneType));
		when(zoneAttrRepository.save(any(ZoneAttrEntity.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
		when(violationTypeRepository.findByIdAndIsActiveIsTrue(any())).thenReturn(new ViolationTypeEntity());
		ZoneCreateRequest zoneCreateRequest = new ZoneCreateRequest();
		ZonePointRequest zonePointRequest = new ZonePointRequest();
		zonePointRequest.setX(1.0);
		zonePointRequest.setY(1.0);
		zoneCreateRequest.setCoords(Arrays.asList(zonePointRequest));
		zoneCreateRequest.setDeviceId("VMS");
		ZoneAttrRequest zoneAttr = new ZoneAttrRequest();
		zoneAttr.setZoneTypeId(1);
		zoneAttr.setValue(5.0f);
		zoneCreateRequest.setZoneAttrs(new HashSet(Collections.singletonList(zoneAttr)));
		
		when(deviceRepository.isPermitted(any(), any())).thenReturn(true);
		Response actualResponse = service.createNew(zoneCreateRequest, authentication);
		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
	}

	@Test
	void deleteOne() {

		ZoneEntity zone = ZoneEntity.builder().id(1).coords("[{\"x\":0.031,\"y\":0.056},{\"x\":0.983,\"y\":0.043},{\"x\":0.973,\"y\":0.836},{\"x\":0.021,\"y\":0.821},{\"x\":0.033,\"y\":0.063}]")
				.deviceId("VMS").isActive(true).build();
		when(zoneRepository.findByIdAndIsActiveIsTrue(anyInt())).thenReturn(zone);
		when(deviceRepository.isPermitted(any(), any())).thenReturn(true);
		Response actualResponse = service.deleteOne(1, authentication);
		assertThat(actualResponse.getCode(), Matchers.equalTo(204));
	}

	@Test
	void updateOne1ThrowException1() throws Exception {
		Integer id = 0;
		ZoneInfoRequest zoneInfoRequest = new ZoneInfoRequest();
		Assertions.assertThrows(Exception.class, () -> {
			service.updateOne(id,zoneInfoRequest,authentication);
		});
	}


	@Test
	void deleteOne6ThrowException1() throws Exception {
		Integer id = 0;
		Assertions.assertThrows(Exception.class, () -> {
			service.deleteOne(id, authentication);
		});
	}


	@Test
	void stopScp4() throws Exception {
		String deviceId = new String("test");
		BaseResponse actualResult = service.stopScp(deviceId);
		assertThat(actualResult, Matchers.notNullValue());
	}


	@Test
	void getOneThrowException() {
		int id = 1;
		when(zoneRepository.findByIdAndIsActiveIsTrue(eq(id))).thenReturn(null);
		Assertions.assertThrows(Exception.class, () -> service.getOne(id, authentication));
	}

	@Test
	void getOneThrow() {
		int id = 1;
		ZoneEntity zoneEntity = ZoneEntity.builder().id(id).coords("[]").build();
		when(zoneRepository.findByIdAndIsActiveIsTrue(eq(id))).thenReturn(zoneEntity);
		when(zoneAttrRepository.findByZoneIdAndIsActiveIsTrue(eq(id))).thenReturn(new ArrayList<>());
		when(deviceRepository.isPermitted(any(), any())).thenReturn(true);
		Response actualResponse = service.getOne(id, authentication);
		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
	}
}