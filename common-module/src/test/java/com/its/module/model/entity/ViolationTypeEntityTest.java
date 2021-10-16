package com.its.module.model.entity;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class ViolationTypeEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ViolationTypeEntity (){
		assertThat(ViolationTypeEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}