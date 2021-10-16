package com.its.highway_service.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.its.highway_service.configuration.WebConfig;
import com.its.highway_service.model.request.SupervisorRequest;
import com.its.highway_service.repository.HighwayRepository;
import com.its.highway_service.service.HighwayService;
import com.its.module.model.exception.NotfoundException;
import com.its.module.utils.GsonUtils;
import com.its.module.utils.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.its.highway_service.model.dto.UserHighwayDto;
import com.its.highway_service.model.request.UserHighwayRequest;
import com.its.highway_service.repository.UserHighwayRepository;
import com.its.highway_service.service.UserHighwayService;
import com.its.module.model.entity.UserHighwayEntity;
import com.its.module.model.exception.BadRequestException;
import com.its.module.model.response.Response;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserHighwayServiceImpl implements UserHighwayService {
	@Autowired
	private UserHighwayRepository userHighwayRepository;

	@Autowired
	private HighwayRepository highwayRepository;

	@Transactional
	@Override
	public Response<?> modify(SupervisorRequest supervisorRequest, Integer authId) {
		Integer userId = supervisorRequest.getUserId();
		if(userHighwayRepository.countUserById(userId) != 1)
			throw new BadRequestException(WebConfig.translate("request.validate"), 1020, "user id khong ton tai");
		Set<Integer> highwayIds = supervisorRequest.getHighwayIds();
		List<UserHighwayEntity> savingList = new ArrayList<>();
		for (UserHighwayEntity userHighway:userHighwayRepository.findByUserIdAndIsActiveIsTrue(userId)) {
			Integer highwayId = userHighway.getHighwayId();
			if(!highwayIds.remove(highwayId)) {
				userHighway.setIsActive(false);
				ObjectUtils.setModifyHistory(userHighway, authId);
				savingList.add(userHighway);
			}
		}
		for(Integer highwayId:highwayIds) {
			if(highwayRepository.findByIdAndIsActiveIsTrue(highwayId) == null)
				throw new BadRequestException(WebConfig.translate("request.validate"), 1021, "highwayId khong hop le: " + highwayId);
			UserHighwayEntity userHighway = UserHighwayEntity.builder()
					.userId(userId)
					.highwayId(highwayId)
					.build();
			ObjectUtils.setCreateHistory(userHighway, authId);
			savingList.add(userHighway);
		}
		userHighwayRepository.saveAll(savingList);
		return Response.builder()
				.code(HttpStatus.OK.value())
				.message(WebConfig.translate("process_success"))
				.build();
	}
}
