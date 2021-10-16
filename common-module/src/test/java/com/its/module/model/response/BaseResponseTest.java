package com.its.module.model.response;import com.its.module.model.response.BaseResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class BaseResponseTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void BaseResponse (){
		assertThat(BaseResponse.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}