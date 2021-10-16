package com.its.gateway.configuration.filter;

import com.google.gson.Gson;
import com.its.gateway.model.exception.FeignClientException;
import com.its.gateway.repository.AuthServiceFeignClient;
import com.its.module.model.dto.UserDto;
import com.its.module.model.request.TokenRequest;
import com.its.module.model.response.Response;
import com.its.module.utils.Constants.*;
import com.its.module.utils.StringUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PreFilter extends ZuulFilter {
    private static Logger LOGGER = LoggerFactory.getLogger(PreFilter.class);

    @Autowired
    private AuthServiceFeignClient authServiceFeignClient;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Autowired
    private Gson gson;

    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String auth = request.getHeader(AuthHeader.AUTHORIZATION);
        HttpServletResponse response = ctx.getResponse();

        // always set is_internal is false
        ctx.addZuulRequestHeader(AuthHeader.IS_INTERNAL, "0");

        if(auth == null) {
            SecurityContextHolder.clearContext();
            ctx.addZuulRequestHeader(AuthHeader.USER_ID, null);
            ctx.addZuulRequestHeader(AuthHeader.SSO_ID, null);
            ctx.addZuulRequestHeader(AuthHeader.USERNAME, null);
            ctx.addZuulRequestHeader(AuthHeader.ROLE, null);
            ctx.addZuulRequestHeader(AuthHeader.PERMISSION, null);
            return null;
        }
        TokenRequest tokenRequest = new TokenRequest(auth);
        Response<UserDto> odpResponse = null;
        try {
            odpResponse = authServiceFeignClient.getUserByToken("1", tokenRequest);
        } catch (RuntimeException e) {
            LOGGER.error("error", e);
            try {
                if(e.getCause() instanceof FeignClientException) {
                    FeignClientException feignClientException = (FeignClientException) e.getCause();
                    if(feignClientException.getCode() == 500) response.sendError(500, "Có vấn đề xảy ra với máy chủ xác thực");
                    else response.sendError(401);
                } else response.sendError(500, "Không thể kết nối đến máy chủ xác thực");
                ctx.setSendZuulResponse(false);
            } catch (IOException ex) {
                LOGGER.error("error", ex);
            }
            return null;
        }

        if(odpResponse != null
                && odpResponse.getCode() == HttpStatus.OK.value()
                && odpResponse.getData() != null) {
            UserDto userDto = odpResponse.getData();
            ctx.addZuulRequestHeader(AuthHeader.USER_ID, String.valueOf(userDto.getId()));
            ctx.addZuulRequestHeader(AuthHeader.SSO_ID, userDto.getSsoId());
            ctx.addZuulRequestHeader(AuthHeader.USERNAME, userDto.getUserName());
            ctx.addZuulRequestHeader(AuthHeader.ROLE, String.valueOf(userDto.getRole()));
            String permissions = userDto.getPermissions().stream().collect(Collectors.joining(";"));
            ctx.addZuulRequestHeader(AuthHeader.PERMISSION, permissions);
        } else {
            try {
                response.sendError(HttpStatus.UNAUTHORIZED.value());
                ctx.setSendZuulResponse(false);
            } catch (IOException e) {
                LOGGER.error("error", e);
            }
        }
        return null;
    }
}
