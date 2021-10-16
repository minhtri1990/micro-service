package com.its.module.model.entity;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import lombok.NoArgsConstructor;import javax.persistence.*;import java.time.LocalDateTime;import com.its.module.model.entity.UserHighwayEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class UserHighwayEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void UserHighwayEntity (){
		BeanMatchers.registerValueGenerator(new ValueGenerator<LocalDateTime>() {
			public LocalDateTime generate() {
				return null;  // Change to generate random instance
		}
		}, LocalDateTime.class);
		assertThat(UserHighwayEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}