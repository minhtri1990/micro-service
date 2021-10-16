package com.its.auth_service.model.request;import com.its.module.model.annotation.NotNull;import com.its.module.model.annotation.Trim;import com.its.module.model.annotation.MaxLength;import lombok.Data;import com.its.auth_service.model.request.RoleRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class RoleRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void RoleRequest (){
		assertThat(RoleRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}