package com.its.auth_service.model.request;import com.its.module.model.annotation.NotNull;import lombok.Data;import com.its.auth_service.model.request.TokenRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class TokenRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void TokenRequest (){
		assertThat(TokenRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}