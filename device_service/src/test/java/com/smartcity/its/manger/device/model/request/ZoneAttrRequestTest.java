package com.smartcity.its.manger.device.model.request;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;

class ZoneAttrRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ZoneAttrRequest (){
		assertThat(ZoneAttrRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}