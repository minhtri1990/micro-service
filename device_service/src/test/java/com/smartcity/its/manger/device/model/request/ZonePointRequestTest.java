package com.smartcity.its.manger.device.model.request;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;

class ZonePointRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ZonePointRequest (){
		assertThat(ZonePointRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}