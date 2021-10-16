package com.smartcity.its.manager.violation.model.request;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import lombok.NoArgsConstructor;import com.smartcity.its.manager.violation.model.request.SmsRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class SmsRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void SmsRequest (){
		assertThat(SmsRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}