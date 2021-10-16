package com.its.highway_service.service.impl;

import com.its.highway_service.model.request.SupervisorRequest;
import com.its.highway_service.model.request.UserHighwayRequest;
import com.its.highway_service.repository.HighwayRepository;
import com.its.highway_service.repository.UserHighwayRepository;
import com.its.module.model.entity.HighwayEntity;
import com.its.module.model.entity.UserHighwayEntity;
import com.its.module.model.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class UserHighwayServiceImplTest {

	@Mock
	private UserHighwayRepository userHighwayRepository;

	@Mock
	private HighwayRepository highwayRepository;

	@InjectMocks 
	UserHighwayServiceImpl service;

	@BeforeEach
	void setUp() {
		service = new UserHighwayServiceImpl();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void modify() {
		SupervisorRequest request = new SupervisorRequest();
		int userId = 7;
		int highwayId1 = 1, highwayId2 = 2;
		request.setHighwayIds(new HashSet<>(Arrays.asList(highwayId1, highwayId2)));
		request.setUserId(userId);
		when(userHighwayRepository.countUserById(eq(userId))).thenReturn(1);
		UserHighwayEntity userHighwayEntity1 = UserHighwayEntity.builder().userId(userId).highwayId(highwayId1).build();
		when(userHighwayRepository.findByUserIdAndIsActiveIsTrue(eq(userId))).thenReturn(Collections.singletonList(userHighwayEntity1));
		when(highwayRepository.findByIdAndIsActiveIsTrue(eq(highwayId2))).thenReturn(new HighwayEntity());
		Response actualResponse = service.modify(request, 7);
		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
	}
}