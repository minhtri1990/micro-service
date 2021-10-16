package com.smartcity.its.manager.violation.configuration;

import com.its.module.configuration.ServiceFilterAdapter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

/**
 * 
 * Sử dụng để chuyển đổi user_id, role, token ở header thành đối tượng authentication ở mỗi endpoint
 *
 */
@Component
public class ServiceFilter extends ServiceFilterAdapter {
}
