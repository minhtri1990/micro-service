package com.its.highway_service.model.request;import com.its.module.model.annotation.MaxLength;import com.its.module.model.annotation.NotNull;import com.its.module.model.annotation.Trim;import lombok.Data;import com.its.highway_service.model.request.HighwayRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class HighwayRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void HighwayRequest (){
		assertThat(HighwayRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}