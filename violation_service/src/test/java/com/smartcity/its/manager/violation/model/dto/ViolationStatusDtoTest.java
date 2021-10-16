package com.smartcity.its.manager.violation.model.dto;import com.fasterxml.jackson.annotation.JsonProperty;import lombok.Data;import com.smartcity.its.manager.violation.model.dto.ViolationStatusDto;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class ViolationStatusDtoTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ViolationStatusDto (){
		assertThat(ViolationStatusDto.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}