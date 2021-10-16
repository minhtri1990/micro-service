package com.its.highway_service.controller;

import com.its.highway_service.model.request.SupervisorRequest;
import com.its.highway_service.model.request.UserHighwayRequest;
import com.its.highway_service.service.AuthService;
import com.its.highway_service.service.UserHighwayService;
import com.its.module.model.exception.ForbiddenException;
import com.its.module.utils.Constants;
import com.its.module.utils.ObjectUtils;
import com.its.module.utils.PermissionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.its.highway_service.model.request.HighwayRequest;
import com.its.highway_service.service.HighwayService;
import com.its.module.model.exception.UnauthorizedException;
import com.its.module.model.response.Response;
import com.its.module.utils.Constants.AuthHeader;

import javax.validation.Valid;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/highways")
public class HighwayController {
	@Autowired
	private HighwayService highwayService;

	@Autowired
	private UserHighwayService userHighwayService;

	@Autowired
	private AuthService authService;

	@GetMapping
	public Response<?> getAllHighways(@RequestParam Map<String, String> params,
									  @RequestParam(value = Constants.Pagination.PAGE_REQUEST_PARAM, defaultValue = "0") Integer offset,
									  @RequestParam(value = Constants.Pagination.PAGE_SIZE_REQUEST_PARAM, defaultValue = Constants.Pagination.PAGE_SIZE_DEFAULT) Integer limit,
									  Authentication authentication) {
		boolean isAdmin = PermissionUtils.hasAnyAuthority(authentication,"0","7");
		return highwayService.getAllHighway(params, (Integer) authentication.getPrincipal(), isAdmin, offset, limit);
	}

	@PostMapping
	@PreAuthorize("hasAnyAuthority('0','7')")
	public Response<?> createHighway(@RequestBody @Valid HighwayRequest highwayRequest,
									 Authentication authentication) {
		return highwayService.createHighway(highwayRequest, (Integer) authentication.getPrincipal());
	}

	@GetMapping("/{id}")
	@PreAuthorize("@authServiceImpl.isPermittedAccessHighway(#authId, #id) or hasAnyAuthority('0','7')")
	public Response<?> getById(@PathVariable("id") Integer id,
							   Authentication authentication) {
		return highwayService.getHighwayById(id, (Integer) authentication.getPrincipal());
	}

	@PutMapping("/{id}")
	public Response<?> changeHighwayInfo(@PathVariable("id") Integer id,
										 @RequestBody @Valid HighwayRequest highwayRequest,
										 Authentication authentication) {
		return highwayService.changeHighwayInfo(id, highwayRequest, (Integer) authentication.getPrincipal());
	}

	@DeleteMapping(value = "/{id}")
	public Response<?> deleteHighway(@PathVariable("id") Integer id,
									 Authentication authentication) {
		return highwayService.deleteHighway(id, (Integer) authentication.getPrincipal());
	}

	@PostMapping("/supervisors")
	public Response modifySupervisor(@RequestBody @Valid SupervisorRequest supervisorRequest,
								  	Authentication authentication) {
		return userHighwayService.modify(supervisorRequest, (Integer) authentication.getPrincipal());
	}
}
