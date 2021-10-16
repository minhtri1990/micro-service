package com.its.module.utils;

import com.its.module.model.response.Response;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponseUtilsTest {
    @Test
    void emptyListResponse() {
        Response response = ResponseUtils.emptyListResponse(5);
        MatcherAssert.assertThat(response, Matchers.notNullValue());
    }
}