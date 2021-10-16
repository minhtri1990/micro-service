package com.its.auth_service.model.entity;
import com.its.auth_service.utils.OtpType;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;
import com.its.auth_service.model.entity.OtpEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class OtpEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void OtpEntity (){
		BeanMatchers.registerValueGenerator(new ValueGenerator<LocalDateTime>() {
			public LocalDateTime generate() {
				return null;  // Change to generate random instance
		}
		}, LocalDateTime.class);
		assertThat(OtpEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}