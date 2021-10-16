package com.its.module.model.response;import com.fasterxml.jackson.annotation.JsonInclude;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import lombok.NoArgsConstructor;import com.its.module.model.response.Response;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class ResponseTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void Response (){
		assertThat(Response.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}