package com.its.highway_service.model.dto;import lombok.Data;import lombok.Getter;import lombok.Setter;import java.time.LocalDateTime;import com.its.highway_service.model.dto.HighwayDto;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class HighwayDtoTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void HighwayDto (){
		BeanMatchers.registerValueGenerator(new ValueGenerator<LocalDateTime>() {
			public LocalDateTime generate() {
				return null;  // Change to generate random instance
		}
		}, LocalDateTime.class);
		assertThat(HighwayDto.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}