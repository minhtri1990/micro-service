package com.its.auth_service.model.entity;import lombok.*;import javax.persistence.*;import java.sql.Date;import java.time.LocalDateTime;import com.its.auth_service.model.entity.UserEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class UserEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void UserEntity (){
		BeanMatchers.registerValueGenerator(new ValueGenerator<LocalDateTime>() {
			public LocalDateTime generate() {
				return null;  // Change to generate random instance
		}
		}, LocalDateTime.class);
		assertThat(UserEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}