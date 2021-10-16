package com.smartcity.its.manager.violation.model.request;import lombok.Data;import com.smartcity.its.manager.violation.model.request.ViolationInfoRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class ViolationInfoRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ViolationInfoRequest (){
		assertThat(ViolationInfoRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}