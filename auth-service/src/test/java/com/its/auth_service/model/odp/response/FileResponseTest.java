package com.its.auth_service.model.odp.response;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.its.auth_service.model.odp.response.FileResponse;
import static org.hamcrest.MatcherAssert.assertThat;
import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.hamcrest.Matchers;
import static org.hamcrest.CoreMatchers.allOf;
import static com.google.code.beanmatchers.BeanMatchers.*;
import org.junit.jupiter.api.Test;

class FileResponseTest {

	@BeforeEach
	void setUp() {
	}

	@Test
	void FileResponse (){
		assertThat(FileResponse.class, allOf(hasValidBeanConstructor()));
	}

}