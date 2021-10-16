//package com.smartcity.its.manager.violation.service.impl;
//
//import com.its.module.model.response.BaseResponse;
//import com.smartcity.its.manager.violation.repository.*;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//
//class WarningServiceImplTest {
//
//	@Mock
//	private ViolationTypeRepository violationTypeRepository;
//	@Mock
//	private DeviceRepository deviceRepository;
//	@Mock
//	private WarningLogRepository warningLogRepository;
//	@Mock
//	private WarningConfigRepository warningConfigRepository;
//	@Mock
//	private WarningConfigItemRepository warningConfigItemRepository;
//	@Mock
//	private WarningConfirmRepository warningConfirmRepository;
//	@Mock
//	private EmailFeignClient emailFeignClient;
//	@InjectMocks 
//	WarningServiceImpl service;
//
//	@BeforeEach
//	void setUp() {
//		service = new WarningServiceImpl();
//		MockitoAnnotations.initMocks(this);
//	}
//
//	@Test
//	void stopReport0() throws Exception {
//		//TODO: sua dau vao phu hop voi nghiep vu
//		Integer userId = 0;
//
//		//TODO: sua dau ra cua mock method de tao thanh cac testcase khac
//
//		// invoke method
//		BaseResponse actualResult = service.stopReport(userId);
//
//		//TODO: thay doi assert result phu hop voi nghiep vu
//		assertThat(actualResult, Matchers.notNullValue());
//	}
//
//	//@Test
//	void stopReport0ThrowException1() throws Exception {
//		//TODO: sua dau vao phu hop voi nghiep vu
//		Integer userId = 0;
//
//		//TODO: sua dau ra cua mock method de tao thanh cac testcase khac
//
//		//TODO: thay doi assert result phu hop voi nghiep vu
//		Assertions.assertThrows(Exception.class, () -> {
//			service.stopReport(userId);
//		});
//	}
//
//}