package com.its.auth_service.repository;

import com.its.module.model.response.Response;

public class EmailFeignClientFallback implements EmailFeignClient {
    @Override
    public Response<String> sendEmail(String subject, String fromemail, String toemail, String body) {
        return null;
    }
}
