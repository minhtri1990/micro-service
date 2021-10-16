package com.its.module.model.entity;import lombok.*;import javax.persistence.*;import java.time.LocalDateTime;import com.its.module.model.entity.HighwayEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class HighwayEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void HighwayEntity (){
		BeanMatchers.registerValueGenerator(new ValueGenerator<LocalDateTime>() {
			public LocalDateTime generate() {
				return null;  // Change to generate random instance
		}
		}, LocalDateTime.class);
		assertThat(HighwayEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}