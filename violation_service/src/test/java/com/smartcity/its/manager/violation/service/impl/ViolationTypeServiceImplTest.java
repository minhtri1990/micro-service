package com.smartcity.its.manager.violation.service.impl;

import com.its.module.model.response.Response;
import com.smartcity.its.manager.violation.repository.ViolationTypeRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ViolationTypeServiceImplTest {

	@Mock
	private ViolationTypeRepository violationTypeRepository;
	@Mock
	private ModelMapper mapper;
	@InjectMocks 
	ViolationTypeServiceImpl service;

	@BeforeEach
	void setUp() {
		service = new ViolationTypeServiceImpl();
		MockitoAnnotations.initMocks(this);
	}


	@Test
	void getAll() {
		int type = 0;
		when(violationTypeRepository.findByTypeAndIsActiveIsTrue(eq(type))).thenReturn(new ArrayList<>());
		Response actualResponse = service.getAll(type);
		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
	}
}