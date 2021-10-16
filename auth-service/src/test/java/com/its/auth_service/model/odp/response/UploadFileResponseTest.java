package com.its.auth_service.model.odp.response;import java.util.List;import lombok.Data;import lombok.NoArgsConstructor;import com.its.auth_service.model.odp.response.UploadFileResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class UploadFileResponseTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void UploadFileResponse (){
		assertThat(UploadFileResponse.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}