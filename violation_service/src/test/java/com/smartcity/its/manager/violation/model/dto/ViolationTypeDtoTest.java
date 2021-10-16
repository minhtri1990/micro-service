package com.smartcity.its.manager.violation.model.dto;import lombok.Data;import java.time.LocalDateTime;import com.smartcity.its.manager.violation.model.dto.ViolationTypeDto;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class ViolationTypeDtoTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ViolationTypeDto (){
		assertThat(ViolationTypeDto.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}