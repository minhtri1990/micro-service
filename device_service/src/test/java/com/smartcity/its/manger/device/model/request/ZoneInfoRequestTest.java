package com.smartcity.its.manger.device.model.request;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.smartcity.its.manager.device.model.dto.zone.ZoneInfoRequest;

class ZoneInfoRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ZoneInfoRequest (){
		assertThat(ZoneInfoRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}