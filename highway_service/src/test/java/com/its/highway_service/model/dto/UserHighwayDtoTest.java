package com.its.highway_service.model.dto;import com.fasterxml.jackson.annotation.JsonInclude;import lombok.Data;import java.time.LocalDateTime;import com.its.highway_service.model.dto.UserHighwayDto;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class UserHighwayDtoTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void UserHighwayDto (){
		BeanMatchers.registerValueGenerator(new ValueGenerator<LocalDateTime>() {
			public LocalDateTime generate() {
				return null;  // Change to generate random instance
		}
		}, LocalDateTime.class);
		assertThat(UserHighwayDto.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}