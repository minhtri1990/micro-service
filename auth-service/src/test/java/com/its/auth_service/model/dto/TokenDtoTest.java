package com.its.auth_service.model.dto;import lombok.Data;import com.its.auth_service.model.dto.TokenDto;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class TokenDtoTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void TokenDto (){
		assertThat(TokenDto.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}