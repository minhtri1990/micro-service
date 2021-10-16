package com.smartcity.its.manager.media.dto;

import com.google.code.beanmatchers.BeanMatchers;
import com.google.code.beanmatchers.ValueGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FilePropertiesTest {
    @BeforeEach
    void setUp() {
    }

    @Test
    void FileProperties (){
        BeanMatchers.registerValueGenerator(new ValueGenerator<LocalDateTime>() {
            public LocalDateTime generate() {
                return null;  // Change to generate random instance
            }
        }, LocalDateTime.class);
        assertThat(FileProperties.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters()));
    }

}