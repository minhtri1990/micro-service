package com.smartcity.its.manger.device.model.request.scp;import com.fasterxml.jackson.annotation.JsonProperty;import lombok.Data;import java.util.List;import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;import com.smartcity.its.manager.device.model.request.scp.ScpRequest;import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class ScpRequestTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ScpRequest (){
		assertThat(ScpRequest.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}