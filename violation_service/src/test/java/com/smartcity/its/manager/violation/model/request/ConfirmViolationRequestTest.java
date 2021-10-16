package com.smartcity.its.manager.violation.model.request;import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfirmViolationRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ConfirmViolationRequest (){
		assertThat(ConfirmViolationRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}