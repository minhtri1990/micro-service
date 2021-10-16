package com.its.highway_service.service.impl;

import com.its.highway_service.configuration.WebConfig;
import com.its.highway_service.model.dto.HighwayDto;
import com.its.highway_service.model.dto.HighwaySearchDto;
import com.its.highway_service.model.request.HighwayRequest;
import com.its.highway_service.repository.HighwayRepository;
import com.its.highway_service.service.HighwayService;
import com.its.module.model.entity.HighwayEntity;
import com.its.module.model.exception.BadRequestException;
import com.its.module.model.exception.NotfoundException;
import com.its.module.model.response.Meta;
import com.its.module.model.response.Response;
import com.its.module.utils.GsonUtils;
import com.its.module.utils.MathUtils;
import com.its.module.utils.ObjectUtils;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HighwayServiceImpl implements HighwayService {
	@Autowired
	private HighwayRepository highwayRepository;

	@Override
	public Response<?> getAllHighway(Map<String, String> params, Integer authId, boolean isAdmin, Integer page, Integer pageSize) {
		if(page == 0) {
			page = 1;
			pageSize = 1000000;
		}
		if(page < 0 || pageSize <= 0)
			throw new BadRequestException(WebConfig.translate("request.validate"));

		String name = params.get("name");
		Integer userId = params.containsKey("userId")?Integer.valueOf(params.get("userId")):null;
		int offset = (page-1)*pageSize;
		List<HighwaySearchDto> highways;
		if(isAdmin) {
			highways = highwayRepository.searchAdminRole(userId, name, pageSize, offset);
		} else {
			System.out.println("ok");
			if(userId != null)
				throw new BadRequestException(WebConfig.translate("request.validate"), 1022, "chi tim kiem theo userId bang quyen admin");
			highways = highwayRepository.searchUserRole(authId, name, pageSize, offset);
		}
		int totalEl = highways.size() == 0? 0: highways.remove(highways.size()-1).getId();
		Meta meta = Meta.builder()
				.page(page)
				.pageSize(pageSize)
				.totalPages(MathUtils.calcNumPages(totalEl, pageSize))
				.totalElements(totalEl)
				.build();

		return Response.builder()
				.code(200)
				.meta(meta)
				.data(highways)
				.build();
	}

	/**
	 * get highway by id, if request is from supervisor or system admin
	 * 
	 * @param highwayId
	 * @param authId
	 * @return
	 */
	@Override
	public Response<?> getHighwayById(Integer highwayId, Integer authId) {
		HighwayEntity highwayEntity = highwayRepository.findByIdAndIsActiveIsTrue(highwayId);
		if (highwayEntity == null)
			throw new NotfoundException(WebConfig.translate("not_found"));
		HighwayDto highwayDto = GsonUtils.mapObject(highwayEntity, HighwayDto.class);
		return Response.builder().code(200).data(highwayDto).build();
	}

	@Override
	public Response<?> createHighway(HighwayRequest highwayRequest, Integer authId) {
		HighwayEntity highwayEntity = HighwayEntity.builder()
				.isActive(true)
				.name(highwayRequest.getName())
				.tntId(highwayRequest.getTntId())
				.build();
		ObjectUtils.setCreateHistory(highwayEntity, authId);
		highwayEntity = highwayRepository.save(highwayEntity);
		HighwayDto highwayDto = GsonUtils.mapObject(highwayEntity, HighwayDto.class);
		return Response.builder().code(HttpStatus.CREATED.value()).data(highwayDto).build();
	}

	@Override
	public Response<?> changeHighwayInfo(Integer highwayId, HighwayRequest highwayRequest, Integer authId) {
		HighwayEntity highwayEntity = highwayRepository.findByIdAndIsActiveIsTrue(highwayId);
		if (highwayEntity == null)
			throw new NotfoundException(WebConfig.translate("not_found"));
		highwayEntity.setName(highwayRequest.getName());
		highwayEntity.setTntId(highwayRequest.getTntId());
		ObjectUtils.setModifyHistory(highwayEntity, authId);
		highwayEntity = highwayRepository.save(highwayEntity);
		return Response.builder().code(200).data(GsonUtils.mapObject(highwayEntity, HighwayDto.class)).build();
	}

	@Override
	public Response<?> deleteHighway(Integer id, Integer authId) {
		HighwayEntity highwayEntity = highwayRepository.findByIdAndIsActiveIsTrue(id);
		if (highwayEntity == null)
			throw new NotfoundException(WebConfig.translate("not_found"));
		highwayEntity.setIsActive(false);
		ObjectUtils.setModifyHistory(highwayEntity, authId);
		highwayRepository.save(highwayEntity);
		return Response.builder().code(HttpStatus.NO_CONTENT.value()).message(WebConfig.translate("process_success")).build();
	}
}
