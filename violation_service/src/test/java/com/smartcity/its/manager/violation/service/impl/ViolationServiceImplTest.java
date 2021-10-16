package com.smartcity.its.manager.violation.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.its.module.model.entity.ViolationTypeEntity;
import com.smartcity.its.manager.violation.model.entity.ViolationEntity;
import com.smartcity.its.manager.violation.model.entity.ViolationStatusEntity;
import com.smartcity.its.manager.violation.model.entity.WarningConfigEntity;
import com.smartcity.its.manager.violation.model.entity.WarningConfigItemEntity;
import com.smartcity.its.manager.violation.model.request.ConfirmViolationRequest;
import com.smartcity.its.manager.violation.model.request.scp.ScpReceiveRequest;
import com.smartcity.its.manager.violation.model.request.scp.ScpViolation;
import com.smartcity.its.manager.violation.repository.DeviceRepository;
import com.smartcity.its.manager.violation.repository.EmailFeignClient;
import com.smartcity.its.manager.violation.repository.ViolationRepository;
import com.smartcity.its.manager.violation.repository.ViolationStatusRepository;
import com.smartcity.its.manager.violation.repository.ViolationTypeRepository;
import com.smartcity.its.manager.violation.repository.WarningConfigItemRepository;
import com.smartcity.its.manager.violation.repository.WarningConfigRepository;
import com.smartcity.its.manager.violation.repository.WarningConfirmRepository;
import com.smartcity.its.manager.violation.repository.WarningLogRepository;
import com.smartcity.its.manager.violation.utils.ViolationUtils;

import feign.Response;

class ViolationServiceImplTest {

	@Mock
	DeviceRepository deviceRepository;
	@Mock
	ViolationRepository violationRepository;
	@Mock
	private ViolationTypeRepository violationTypeRepository;
	@Mock
	private WarningConfigRepository warningConfigRepository;
	@Mock
	private WarningConfigItemRepository warningConfigItemRepository;
	@Mock
	private WarningLogRepository warningLogRepository;
	@Mock
	private ViolationStatusRepository violationStatusRepository;
	@Mock
	private EmailFeignClient emailFeignClient;
	@Mock
	private WarningConfirmRepository warningConfirmRepository;
	@InjectMocks 
	ViolationServiceImpl service;
	
	Authentication authentication = new UsernamePasswordAuthenticationToken(16, "Token", Collections.singleton(new SimpleGrantedAuthority("ROLE_admin")));

	@BeforeEach
	void setUp() {
		service = new ViolationServiceImpl();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void getOne4ThrowException2() throws Exception {
		String id = new String("test");
		Integer authId = 0;
		Integer role = 0;
		Assertions.assertThrows(Exception.class, () -> {
			service.getOne(id, authentication);
		});
	}

	@Test
	void confirmNotViolated2ThrowException2() throws Exception {
		String violationId = new String("test");
		ConfirmViolationRequest confirmViolationRequest = new ConfirmViolationRequest();
		Assertions.assertThrows(Exception.class, () -> {
			service.confirmNotViolated(violationId,confirmViolationRequest,authentication);
		});
	}

	//@Test
	void searchViolations() throws IOException {
		when(deviceRepository.getCameraIds(any())).thenReturn(Collections.singletonList("VMS-49668"));
		ViolationEntity violationEntity = ViolationEntity.builder()
				.id("1")
				.type(1)
				.build();
		when(violationRepository.searchViolations(any(),any(),any(),any(),any(),any(),any())).thenReturn(Collections.singletonList(violationEntity));
		when(violationRepository.countSearchViolations(any(),any(),any(),any(),any())).thenReturn(2);
		ViolationTypeEntity violationType = ViolationTypeEntity.builder()
				.code("1")
				.id(1)
				.build();
		when(violationTypeRepository.findAll()).thenReturn(Collections.singletonList(violationType));
		when(violationTypeRepository.findByIsActiveIsTrue()).thenReturn(Collections.singletonList(violationType));
		ViolationStatusEntity violationStatus = ViolationStatusEntity.builder()
				.id(1)
				.name("abc")
				.build();
		when(violationStatusRepository.findAll()).thenReturn(Collections.singletonList(violationStatus));
		
		com.its.module.model.response.Response actualResponse = service.search(new HashMap<>(), 1, 10, authentication);
		assertThat(actualResponse, Matchers.notNullValue());
		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
	}

	@Test
	void extract() throws IOException {
		when(deviceRepository.getCameraIds(any())).thenReturn(Collections.singletonList("VMS-49668"));
		ViolationEntity violationEntity = ViolationEntity.builder()
				.id("1")
				.type(1)
				.timestamp(1054546L)
				.isViolation(true)
				.address("134")
				.build();
		when(violationRepository.searchViolations(any(),any(),any(),any(),any(),any(),any())).thenReturn(Collections.singletonList(violationEntity));
		when(violationRepository.countSearchViolations(any(),any(),any(),any(),any())).thenReturn(2);
		ViolationTypeEntity violationType = ViolationTypeEntity.builder()
				.code("1")
				.id(1)
				.build();
		when(violationTypeRepository.findAll()).thenReturn(Collections.singletonList(violationType));
		when(violationTypeRepository.findByIsActiveIsTrue()).thenReturn(Collections.singletonList(violationType));
		ViolationStatusEntity violationStatus = ViolationStatusEntity.builder()
				.id(1)
				.name("abc")
				.build();
		when(violationStatusRepository.findAll()).thenReturn(Collections.singletonList(violationStatus));
		
		
		ResponseEntity actualResponse = service.extract(new HashMap<>(), 1, 10, authentication);
		assertThat(actualResponse.getStatusCode(), Matchers.equalTo(HttpStatus.OK));
	}

	@Test
	void confirmNotViolated() {
		ViolationEntity violation = ViolationEntity.builder()
				.id("1")
				.build();
		when(violationRepository.findByIdAndIsActiveIsTrue(any())).thenReturn(violation);
		
		ConfirmViolationRequest confirmViolationRequest = new ConfirmViolationRequest();
		confirmViolationRequest.setDescription("aaa");
		com.its.module.model.response.Response actualResponse = service.confirmNotViolated("1", confirmViolationRequest, authentication);
		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
	}

	@Test
	void receiveViolationFromScp() {
		String id = "123";
		String deviceId = "222";
		String violationCode = "road_lane";
		ScpViolation scpViolation = new ScpViolation();
		scpViolation.setId(id);
		scpViolation.setDeviceId(deviceId);
		scpViolation.setType(violationCode);
		scpViolation.setTimestamp(482374293L);

		
		ViolationTypeEntity violationType = ViolationTypeEntity.builder().id(1).code(violationCode).build();
		when(violationTypeRepository.findAll()).thenReturn(Collections.singletonList(violationType));
		when(violationTypeRepository.findByViolationCodeAndIsActiveIsTrue(eq(violationCode))).thenReturn(violationType);
		when(violationRepository.save(any())).thenAnswer(in -> in.getArgument(0));

		when(warningConfigRepository.getAllSupervisorIds()).thenReturn(Collections.singletonList(1));
		WarningConfigItemEntity warningConfigItem = WarningConfigItemEntity.builder()
				.sendingType(ViolationUtils.SendingType.IMMEDIATE.ordinal())
				.sendingMethod("0;1")
				.repetition(0)
				.build();
		when(warningConfigItemRepository.findByUserIdAndViolationTypeIdAndIsActiveIsTrue(any(), any(), any())).thenReturn(warningConfigItem);
		WarningConfigEntity warningConfig = WarningConfigEntity.builder()
				.emails("dasdsada")
				.phones("9284093")
				.build();
		when(warningConfigRepository.getByUserIdAndTypeNative(any(), any())).thenReturn(warningConfig);
		com.its.module.model.response.Response actualResponse = service.receiveViolationFromScp(Collections.singletonList(scpViolation));
		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
	}

	@Test
	void getOne() {
		ViolationEntity violationEntity = ViolationEntity.builder()
				.id("1")
				.type(1)
				.build();
		when(violationRepository.findByIdAndIsActiveIsTrue(any())).thenReturn(violationEntity);
		ViolationTypeEntity violationType = ViolationTypeEntity.builder()
				.code("1")
				.id(1)
				.build();
		when(violationTypeRepository.findByIsActiveIsTrue()).thenReturn(Collections.singletonList(violationType));
		ViolationStatusEntity violationStatus = ViolationStatusEntity.builder()
				.id(1)
				.name("abc")
				.build();
		when(violationStatusRepository.findAll()).thenReturn(Collections.singletonList(violationStatus));
		com.its.module.model.response.Response actualResponse = service.getOne("1", authentication);
		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
	}
}