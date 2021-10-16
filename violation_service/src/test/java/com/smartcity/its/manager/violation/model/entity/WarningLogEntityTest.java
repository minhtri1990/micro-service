package com.smartcity.its.manager.violation.model.entity;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import lombok.NoArgsConstructor;import javax.persistence.*;import java.time.LocalDateTime;import com.smartcity.its.manager.violation.model.entity.WarningLogEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class WarningLogEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void WarningLogEntity (){
		BeanMatchers.registerValueGenerator(new ValueGenerator<LocalDateTime>() {
			public LocalDateTime generate() {
				return null;  // Change to generate random instance
		}
		}, LocalDateTime.class);
		assertThat(WarningLogEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}