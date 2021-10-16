package com.its.auth_service.model.dto;import lombok.Data;import java.time.LocalDateTime;import com.its.auth_service.model.dto.RoleDto;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class RoleDtoTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void RoleDto (){
		BeanMatchers.registerValueGenerator(new ValueGenerator<LocalDateTime>() {
			public LocalDateTime generate() {
				return null;  // Change to generate random instance
		}
		}, LocalDateTime.class);
		assertThat(RoleDto.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}