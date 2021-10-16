package com.its.module.utils;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {
    @Test
    void getRandomPassword() {
        String res = StringUtils.getRandomPassword();
        MatcherAssert.assertThat(res, Matchers.notNullValue());
    }

    @Test
    void getRandomString() {
        int len = 8;
        String res = StringUtils.getRandomString(len);
        MatcherAssert.assertThat(res.length(), Matchers.equalTo(len));
    }

    @Test
    void getRandomNumberString() {
        int len = 8;
        String res = StringUtils.getRandomNumberString(len);
        MatcherAssert.assertThat(res.length(), Matchers.equalTo(len));
    }

    @Test
    void extractToken() {
        String token = "dadsa";
        String res = StringUtils.extractToken("Bearer " + token);
        MatcherAssert.assertThat(res, Matchers.equalTo(token));
    }

    @Test
    void concat() {
        String input = "1,2,3,4";
        String res = StringUtils.concat(Arrays.asList(input.split(",")));
        MatcherAssert.assertThat(res, Matchers.equalTo(input));
    }
}