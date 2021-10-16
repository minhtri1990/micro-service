package com.smartcity.its.manager.violation.model.request.scp;import lombok.Data;import java.util.List;import com.smartcity.its.manager.violation.model.request.scp.ScpWrapperRequest;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class ScpWrapperRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ScpWrapperRequest (){
		assertThat(ScpWrapperRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}