package com.smartcity.its.manger.device.model.entity;import com.its.module.model.entity.Editable;import com.smartcity.its.manager.device.model.entity.ZoneAttrEntity;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import lombok.NoArgsConstructor;import javax.persistence.*;import java.time.LocalDateTime;import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class ZoneAttrEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ZoneAttrEntity (){
		BeanMatchers.registerValueGenerator(new ValueGenerator<LocalDateTime>() {
			public LocalDateTime generate() {
				return null;  // Change to generate random instance
		}
		}, LocalDateTime.class);
		assertThat(ZoneAttrEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}