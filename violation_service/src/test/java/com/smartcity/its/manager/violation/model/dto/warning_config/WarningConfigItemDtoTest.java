package com.smartcity.its.manager.violation.model.dto.warning_config;import com.smartcity.its.manager.violation.model.dto.ViolationTypeDto;import lombok.Data;import java.util.List;import com.smartcity.its.manager.violation.model.dto.warning_config.WarningConfigItemDto;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class WarningConfigItemDtoTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void WarningConfigItemDto (){
		assertThat(WarningConfigItemDto.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}