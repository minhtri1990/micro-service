package com.its.auth_service.model.odp.request;import lombok.Data;import java.util.List;import com.its.auth_service.model.odp.request.ODPIdsRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class ODPIdsRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ODPIdsRequest (){
		assertThat(ODPIdsRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}