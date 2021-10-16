package com.its.highway_service.service.impl;

import com.its.highway_service.model.dto.HighwaySearchDto;
import com.its.highway_service.model.request.HighwayRequest;
import com.its.highway_service.repository.HighwayRepository;
import com.its.highway_service.repository.UserHighwayRepository;
import com.its.module.model.entity.HighwayEntity;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HighwayServiceImplTest {

	@Mock
	private HighwayRepository highwayRepository;
	@Mock
	private UserHighwayRepository userHighwayRepository;
	@InjectMocks 
	HighwayServiceImpl service;

	@BeforeEach
	void setUp() {
		service = new HighwayServiceImpl();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void getHighwayById3ThrowException1() throws Exception {
		//TODO: sua dau vao phu hop voi nghiep vu
		Integer highwayId = 0;
		Integer authId = 0;
		Integer role = 0;

		//TODO: sua dau ra cua mock method de tao thanh cac testcase khac

		//TODO: thay doi assert result phu hop voi nghiep vu
		Assertions.assertThrows(Exception.class, () -> {
			service.getHighwayById(highwayId,authId);
		});
	}



	@Test
	void createHighway0ThrowException1() throws Exception {
		//TODO: sua dau vao phu hop voi nghiep vu
		HighwayRequest highwayRequest = new HighwayRequest();
		Integer authId = 0;
		Integer role = 0;

		//TODO: sua dau ra cua mock method de tao thanh cac testcase khac

		//TODO: thay doi assert result phu hop voi nghiep vu
		Assertions.assertThrows(Exception.class, () -> {
			service.createHighway(highwayRequest,authId);
		});
	}


	@Test
	void changeHighwayInfo4ThrowException1() throws Exception {
		Integer highwayId = 0;
		HighwayRequest highwayRequest = new HighwayRequest();
		Integer authId = 0;
		Assertions.assertThrows(Exception.class, () -> {
			service.changeHighwayInfo(highwayId,highwayRequest,authId);
		});
	}

	@Test
	void changeHighwayInfo() {
		HighwayRequest request = new HighwayRequest();
		request.setName("h1");
		request.setTntId("1231231");
		HighwayEntity highwayEntity = HighwayEntity.builder().id(1).build();
		when(highwayRepository.findByIdAndIsActiveIsTrue(eq(1))).thenReturn(highwayEntity);
		when(highwayRepository.save(any(HighwayEntity.class))).thenReturn(highwayEntity);
		Response actualResponse = service.changeHighwayInfo(1, request, 1);
		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
	}


	@Test
	void getAllHighways() {
		int page = 0;
		boolean isAdmin = true;
		HighwaySearchDto highwaySearchDto = mock(HighwaySearchDto.class);
		when(highwaySearchDto.getId()).thenReturn(10);
		List<HighwaySearchDto> highwaySearchDtos = Collections.singletonList(highwaySearchDto);
		when(highwayRepository.searchAdminRole(any(), anyString(), anyInt(), anyInt())).thenReturn(highwaySearchDtos);

		Response actualResponse = service.getAllHighway(new HashMap<>(), 7, isAdmin, page, 3);
		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
	}

	@Test
	void getAllHighwaysUser() {
		int page = 0;
		boolean isAdmin = false;
		HighwaySearchDto highwaySearchDto = mock(HighwaySearchDto.class);
		when(highwaySearchDto.getId()).thenReturn(10);
		List<HighwaySearchDto> highwaySearchDtos = Collections.singletonList(highwaySearchDto);
		when(highwayRepository.searchUserRole(any(), anyString(), anyInt(), anyInt())).thenReturn(highwaySearchDtos);

		Response actualResponse = service.getAllHighway(new HashMap<>(), 7, isAdmin, page, 3);
		assertThat(actualResponse.getCode(), Matchers.equalTo(200));
	}

	@Test
	void deleteHighway() {
		HighwayEntity highwayEntity = HighwayEntity.builder().id(1).build();
		when(highwayRepository.findByIdAndIsActiveIsTrue(eq(1))).thenReturn(highwayEntity);
		Response actualResponse = service.deleteHighway(1, 7);
		assertThat(actualResponse.getCode(), Matchers.equalTo(204));
	}

	@Test
	void deleteHighwayThrowException() {
		when(highwayRepository.findByIdAndIsActiveIsTrue(eq(1))).thenReturn(null);
		Assertions.assertThrows(Exception.class, ()-> service.deleteHighway(1, 7));
	}
}