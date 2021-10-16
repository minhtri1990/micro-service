package com.its.highway_service.model.request;import com.its.module.model.annotation.NotNull;import lombok.Data;import com.its.highway_service.model.request.UserHighwayRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class UserHighwayRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void UserHighwayRequest (){
		assertThat(UserHighwayRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}