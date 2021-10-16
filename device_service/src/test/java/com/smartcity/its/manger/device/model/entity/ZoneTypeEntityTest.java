package com.smartcity.its.manger.device.model.entity;import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import lombok.NoArgsConstructor;import javax.persistence.Column;import javax.persistence.Entity;import javax.persistence.Id;import javax.persistence.Table;import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;import com.smartcity.its.manager.device.model.entity.ZoneTypeEntity;import static org.hamcrest.MatcherAssert.assertThat;
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