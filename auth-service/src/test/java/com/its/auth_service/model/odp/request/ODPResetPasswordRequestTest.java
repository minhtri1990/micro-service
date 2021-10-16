package com.its.auth_service.model.odp.request;import lombok.Data;import com.its.auth_service.model.odp.request.ODPResetPasswordRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class ODPResetPasswordRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ODPResetPasswordRequest (){
		assertThat(ODPResetPasswordRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}