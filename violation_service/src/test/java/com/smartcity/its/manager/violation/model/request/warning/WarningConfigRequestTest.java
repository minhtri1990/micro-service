package com.smartcity.its.manager.violation.model.request.warning;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.Test;

class WarningConfigRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void WarningConfigRequest (){
		assertThat(WarningConfigRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}