package com.smartcity.its.manager.violation.service.impl;

import com.its.module.model.entity.ViolationTypeEntity;
import com.its.module.model.exception.BadRequestException;
import com.its.module.utils.GsonUtils;
import com.smartcity.its.manager.violation.configuration.WebConfig;
import com.its.module.model.exception.NotfoundException;
import com.its.module.model.response.BaseResponse;
import com.its.module.model.response.Response;
import com.its.module.utils.ObjectUtils;
import com.smartcity.its.manager.violation.model.dto.ViolationTypeDto;
import com.smartcity.its.manager.violation.model.dto.warning_config.WarningConfigDto;
import com.smartcity.its.manager.violation.model.dto.warning_config.WarningConfigItemDto;
import com.smartcity.its.manager.violation.model.entity.WarningConfigEntity;
import com.smartcity.its.manager.violation.model.entity.WarningConfigItemEntity;
import com.smartcity.its.manager.violation.model.request.warning.WarningConfigItemRequest;
import com.smartcity.its.manager.violation.model.request.warning.WarningConfigRequest;
import com.smartcity.its.manager.violation.repository.ViolationTypeRepository;
import com.smartcity.its.manager.violation.repository.WarningConfigItemRepository;
import com.smartcity.its.manager.violation.repository.WarningConfigRepository;
import com.smartcity.its.manager.violation.service.WarningConfigService;
import com.smartcity.its.manager.violation.utils.ViolationUtils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class WarningConfigServiceImpl implements WarningConfigService {
    @Autowired
    private WarningConfigRepository warningConfigRepository;

    @Autowired
    private WarningConfigItemRepository warningConfigItemRepository;

    @Autowired
    private ViolationTypeRepository violationTypeRepository;

    @Override
    public BaseResponse getByUserIdAndType(Integer userId, Integer type) {
        WarningConfigEntity warningConfigEntity = getWarningConfigByUserId(userId, type);
        WarningConfigDto warningConfigDto = GsonUtils.mapObject(warningConfigEntity, WarningConfigDto.class);
        warningConfigDto.setItems(getItemConfigDtos(userId, type));
        return Response.builder()
                .code(200)
                .data(warningConfigDto)
                .build();
    }


    @Transactional
    @Override
    public BaseResponse update(Integer userId, WarningConfigRequest warningConfigRequest, Authentication authentication) {
    	Integer authId = (Integer) authentication.getPrincipal();
    	
        WarningConfigEntity warningConfigEntity = getWarningConfigByUserId(userId, warningConfigRequest.getType());
        warningConfigEntity.setPhones(warningConfigRequest.getPhones());
        warningConfigEntity.setEmails(warningConfigRequest.getEmails());
        ObjectUtils.setModifyHistory(warningConfigEntity, authId);
        warningConfigEntity = warningConfigRepository.save(warningConfigEntity);
        WarningConfigDto warningConfigDto = GsonUtils.mapObject(warningConfigEntity, WarningConfigDto.class);

        List<WarningConfigItemEntity> oldItems =
                warningConfigItemRepository.findByUserIdAndTypeAndIsActiveIsTrue(userId, warningConfigRequest.getType());
        Map<Integer, WarningConfigItemEntity> mapItems = new HashMap<>();
        for(WarningConfigItemEntity item:oldItems)
            mapItems.put(item.getViolationTypeId(), item);

        List<ViolationTypeEntity> violationTypes = violationTypeRepository.findByTypeAndIsActiveIsTrue(0);
        Map<Integer, ViolationTypeEntity> mapTypes = new HashMap<>();
        for(ViolationTypeEntity violationType:violationTypes)
            mapTypes.put(violationType.getId(), violationType);
        /**
         * create new config items
         */
        List<WarningConfigItemRequest> itemRequests = warningConfigRequest.getItems();
        List<WarningConfigItemEntity> newItems = new ArrayList<>();
        Set<Integer> violationIdSet = new HashSet<>();
        for(WarningConfigItemRequest itemRequest:itemRequests) {
            Integer violationTypeId = itemRequest.getViolationTypeId();
            if(violationIdSet.contains(violationTypeId))
                throw new BadRequestException(WebConfig.translate("request.validate"));
            violationIdSet.add(violationTypeId);
            WarningConfigItemEntity item;
            if(mapItems.containsKey(violationTypeId)) {
                item = mapItems.get(violationTypeId);
                item.setSendingType(itemRequest.getSendingType());
                item.setSendingMethod(ViolationUtils.convertListToString(itemRequest.getSendingMethod()));
                item.setRepetition(itemRequest.getRepetition());
                ObjectUtils.setModifyHistory(item, authId);
            }
            else {
                item = WarningConfigItemEntity.builder()
                        .type(warningConfigRequest.getType())
                        .sendingMethod(ViolationUtils.convertListToString(itemRequest.getSendingMethod()))
                        .repetition(itemRequest.getRepetition())
                        .violationTypeId(violationTypeId)
                        .sendingType(itemRequest.getSendingType())
                        .userId(authId)
                        .build();
                ObjectUtils.setCreateHistory(item, authId);
            }
            if(item.getSendingType() != ViolationUtils.SendingType.IMMEDIATE.ordinal() && mapTypes.get(violationTypeId).getIsUrgent())
                throw new BadRequestException(WebConfig.translate("warning_config_item.sending_type_not_urgent"));
            newItems.add(item);
        }
        warningConfigItemRepository.saveAll(newItems);

        List<WarningConfigItemEntity> deleteList = new ArrayList<>();
        for(WarningConfigItemEntity item:oldItems) {
            if(!violationIdSet.contains(item.getViolationTypeId())) {
                item.setIsActive(false);
                ObjectUtils.setModifyHistory(item, authId);
                deleteList.add(item);
            }
        }
        warningConfigItemRepository.saveAll(deleteList);

        warningConfigDto.setItems(getItemConfigDtos(userId, warningConfigRequest.getType()));
        return Response.builder()
                .code(HttpStatus.OK.value())
                .message(WebConfig.translate("process_success"))
                .data(warningConfigDto)
                .build();
    }

    private WarningConfigEntity getWarningConfigByUserId(Integer userId, Integer type) {
        WarningConfigEntity warningConfig = warningConfigRepository.getByUserIdAndTypeNative(userId, type);
        if(warningConfig == null) {
//            if (warningConfigRepository.checkValidCreatingId(userId) != 1)
//                throw new NotfoundException(WebConfig.translate("user.not_found"));
            WarningConfigEntity warningConfigEntity = WarningConfigEntity.builder()
                    .id(userId)
                    .type(type)
                    .phones("")
                    .emails("")
                    .build();
            ObjectUtils.setCreateHistory(warningConfigEntity, userId);
            System.out.println("entity" + warningConfigEntity);
            return warningConfigRepository.save(warningConfigEntity);
        }

        return warningConfig;
    }

    //TODO 2 type
    private List<WarningConfigItemDto> getItemConfigDtos(Integer userId, Integer type) {
        List<WarningConfigItemEntity> itemEntities =
                warningConfigItemRepository.findByUserIdAndTypeAndIsActiveIsTrue(userId, type);
        List<ViolationTypeEntity> violationTypes = violationTypeRepository.findByTypeAndIsActiveIsTrue(0);
        Map<Integer, ViolationTypeDto> typeDtoMap = new HashMap<>();
        for(ViolationTypeEntity typeEntity:violationTypes)
            typeDtoMap.put(typeEntity.getId(), GsonUtils.mapObject(typeEntity, ViolationTypeDto.class));
        List<WarningConfigItemDto> itemDtos = new ArrayList<>();
        for (WarningConfigItemEntity itemEntity:itemEntities) {
            WarningConfigItemDto warningConfigItemDto = convertToDto(itemEntity);
            warningConfigItemDto.setViolationTypeDto(typeDtoMap.get(warningConfigItemDto.getViolationTypeId()));
            itemDtos.add(warningConfigItemDto);
        }
        return itemDtos;
    }

    /**
     * chuyen doi sang dto tu string sang list int
     * @param warningConfigItemEntity
     * @return
     */
    private WarningConfigItemDto convertToDto(WarningConfigItemEntity warningConfigItemEntity) {
        WarningConfigItemDto warningConfigItemDto = GsonUtils.mapObject(warningConfigItemEntity, WarningConfigItemDto.class);
        warningConfigItemDto.setSendingMethod(ViolationUtils.convertStringToList(warningConfigItemEntity.getSendingMethod()));
        return warningConfigItemDto;
    }

    /**
     * Kiem tra xem violation co can thong bao khan cap khong?
     * Neu co se luon luon chuyen sending type cua item sang urgent va thong bao ve client
     * @param
     * @param
     * @return message
     */
//    private String checkUrgentSendingType(WarningConfigItemEntity warningConfigItemEntity, Integer newSendingType) {
//        ViolationTypeEntity violationTypeEntity = violationTypeRepository.findByIdAndIsActiveIsTrue(warningConfigItemEntity.getViolationTypeId());
//        if(violationTypeEntity == null)
//            throw new NotfoundException(WebConfig.translate("violation_type.not_found"));
//        String message = null;
//        if(violationTypeEntity.getIsUrgent()) {
//            if(!newSendingType.equals(ViolationUtils.SendingType.IMMEDIATE.ordinal()))
//                message = WebConfig.translate("warning_config_item.sending_type_not_urgent");
//            newSendingType = ViolationUtils.SendingType.IMMEDIATE.ordinal();
//        }
//        warningConfigItemEntity.setSendingType(newSendingType);
//        return message;
//    }

//    class MapConfigItem {
//        private Map<Pair<Pair<Integer, Integer>, Integer>, WarningConfigItemEntity> map = new HashMap<>();
//        public WarningConfigItemEntity get(Integer userId, Integer type, Integer violationType) {
//            return map.get(new Pair<>(new Pair<>(userId, type), violationType));
//        }
//
//        public boolean containsKey(Integer userId, Integer type, Integer violationType) {
//            return map.containsKey(new Pair<>(new Pair(userId, type), violationType));
//        }
//
//        public void put(Integer userId, Integer type, Integer violationType, WarningConfigItemEntity warningConfigItemEntity) {
//            map.put(new Pair<>(new Pair(userId, type), violationType), warningConfigItemEntity);
//        }
//    }
}
