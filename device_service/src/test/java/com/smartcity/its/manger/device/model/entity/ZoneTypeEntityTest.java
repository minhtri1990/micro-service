package com.smartcity.its.manger.device.model.entity;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class ZoneTypeEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ZoneTypeEntity (){
		assertThat(ZoneTypeEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}