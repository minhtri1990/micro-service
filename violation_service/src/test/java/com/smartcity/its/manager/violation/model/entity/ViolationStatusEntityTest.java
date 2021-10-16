package com.smartcity.its.manager.violation.model.entity;import lombok.Data;import javax.persistence.Column;import javax.persistence.Entity;import javax.persistence.Id;import javax.persistence.Table;import com.smartcity.its.manager.violation.model.entity.ViolationStatusEntity;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class ViolationStatusEntityTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void ViolationStatusEntity (){
		assertThat(ViolationStatusEntity.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
	}

}