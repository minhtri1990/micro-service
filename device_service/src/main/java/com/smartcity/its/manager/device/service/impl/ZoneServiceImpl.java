package com.smartcity.its.manager.device.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.its.module.model.entity.ViolationTypeEntity;
import com.its.module.model.exception.BadRequestException;
import com.its.module.model.exception.ForbiddenException;
import com.its.module.model.exception.NotfoundException;
import com.its.module.model.response.BaseResponse;
import com.its.module.model.response.Response;
import com.its.module.utils.Constants.Role;
import com.its.module.utils.GsonUtils;
import com.its.module.utils.ObjectUtils;
import com.its.module.utils.PermissionUtils;
import com.smartcity.its.manager.device.configuration.WebConfig;
import com.smartcity.its.manager.device.model.dto.zone.*;
import com.smartcity.its.manager.device.model.entity.ZoneAttrEntity;
import com.smartcity.its.manager.device.model.entity.ZoneEntity;
import com.smartcity.its.manager.device.model.entity.ZoneTypeEntity;
import com.smartcity.its.manager.device.model.request.scp.ScpArea;
import com.smartcity.its.manager.device.model.request.scp.ScpPoint;
import com.smartcity.its.manager.device.model.request.scp.ScpRequest;
import com.smartcity.its.manager.device.repository.DeviceRepository;
import com.smartcity.its.manager.device.repository.ScpFeignClient;
import com.smartcity.its.manager.device.repository.ViolationTypeRepository;
import com.smartcity.its.manager.device.repository.ZoneAttrRepository;
import com.smartcity.its.manager.device.repository.ZoneRepository;
import com.smartcity.its.manager.device.repository.ZoneTypeRepository;
import com.smartcity.its.manager.device.service.ZoneService;

@Service
public class ZoneServiceImpl implements ZoneService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZoneServiceImpl.class);

    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
    private ZoneRepository zoneRepository;
    
    @Autowired
    private ScpFeignClient scpFeignClient;

    @Autowired
    private ZoneTypeRepository zoneTypeRepository;

    @Autowired
    private ViolationTypeRepository violationTypeRepository;

    @Autowired
    private ZoneAttrRepository zoneAttrRepository;

    @Override
    public Response<?> createNew(ZoneCreateRequest zoneCreateRequest, Authentication authentication) {
        String deviceId = zoneCreateRequest.getDeviceId();
        if(!PermissionUtils.hasAnyRole(authentication, Role.ADMIN) && !deviceRepository.isPermitted(deviceId, authentication))
        	throw new ForbiddenException();
        
        Integer authId = (Integer) authentication.getPrincipal();
        if(zoneCreateRequest.getZoneAttrs().size() == 0)
            throw new BadRequestException(WebConfig.translate("request.validate"), 1003);
        ZoneEntity zone = GsonUtils.mapObject(zoneCreateRequest, ZoneEntity.class);
        zone.setCoords(convertAndCheckValidateListPoint(zoneCreateRequest.getCoords()));
        ObjectUtils.setCreateHistory(zone, authId);
        zone = zoneRepository.save(zone);

        Set<Integer> violationTypeSet = new HashSet<>();
        Set<ZoneAttrDto> zoneAttrDtos = new HashSet<>();
        for(ZoneAttrRequest zoneAttrRequest:zoneCreateRequest.getZoneAttrs()) {
            Optional<ZoneTypeEntity> optional = zoneTypeRepository.findById(zoneAttrRequest.getZoneTypeId());
            if(!optional.isPresent())
                throw new BadRequestException(WebConfig.translate("request.validate"));
            ZoneTypeEntity zoneType = optional.get();
            violationTypeSet.add(zoneType.getViolationTypeId());

            if((zoneType.getMin() != null || zoneType.getMax() != null) && zoneAttrRequest.getValue() == null)
                throw new BadRequestException(WebConfig.translate("request.validate"), 1005, "Yeu cau nhap value cho zoneAttr");
            if(zoneType.getMin() != null && zoneType.getMin() > zoneAttrRequest.getValue())
                throw new BadRequestException(WebConfig.translate("request.validate"), 1006, "zoneAttr.value be hon gia tri min");
            if(zoneType.getMax() != null && zoneType.getMax() < zoneAttrRequest.getValue())
                throw new BadRequestException(WebConfig.translate("request.validate"), 1007, "zoneAttr.value lon hon gia tri max");

            ZoneAttrEntity zoneAttr = ZoneAttrEntity.builder()
                    .zoneId(zone.getId())
                    .zoneTypeId(zoneAttrRequest.getZoneTypeId())
                    .value(zoneAttrRequest.getValue())
                    .build();
            ObjectUtils.setCreateHistory(zoneAttr, authId);
            zoneAttr = zoneAttrRepository.save(zoneAttr);
            ZoneAttrDto zoneAttrDto = GsonUtils.mapObject(zoneAttr, ZoneAttrDto.class);
            zoneAttrDtos.add(zoneAttrDto);
        }

        if(violationTypeSet.size() != 1)
            throw new BadRequestException(WebConfig.translate("request.validate"), 1000);
        Integer id = new ArrayList<>(violationTypeSet).get(0);
        ViolationTypeEntity violationType = violationTypeRepository.findByIdAndIsActiveIsTrue(id);
        if(violationType == null)
            throw new BadRequestException(WebConfig.translate("request.validate"), 1004);
        if("radio".equals(violationType.getChoiceType()) && zoneCreateRequest.getZoneAttrs().size() != 1)
            throw new BadRequestException(WebConfig.translate("request.validate"), 1001);

        ZoneDto zoneDto = GsonUtils.mapObject(zone, ZoneDto.class);
        zoneDto.setCoords(convertStringToListPointDto(zone.getCoords()));
        zoneDto.setZoneAttrs(zoneAttrDtos);

        restartScp(zone.getDeviceId(), authentication);
        //TODO start scp server
        return Response.builder()
                .code(200)
                .message(WebConfig.translate("process_success"))
                .data(zoneDto)
                .build();
    }


    @Override
    public Response<?> getOne(Integer id, Authentication authentication) {
        ZoneEntity zone = zoneRepository.findByIdAndIsActiveIsTrue(id);
        if(zone == null)
            throw new NotfoundException(WebConfig.translate("zone.not_found"));
        if(!PermissionUtils.hasAnyRole(authentication, Role.ADMIN) && !deviceRepository.isPermitted(zone.getDeviceId(), authentication))
        	throw new ForbiddenException();

        ZoneDto zoneDto = GsonUtils.mapObject(zone, ZoneDto.class);
        zoneDto.setCoords(convertStringToListPointDto(zone.getCoords()));

        List<ZoneAttrEntity> zoneAttrs = zoneAttrRepository.findByZoneIdAndIsActiveIsTrue(zone.getId());
        zoneDto.setZoneAttrs(GsonUtils.mapObject(zoneAttrs, new TypeToken<Set<ZoneAttrDto>>(){}.getType()));

        return Response.builder()
                .code(HttpStatus.OK.value())
                .data(zoneDto)
                .build();
    }

   
    @Override
    public BaseResponse getAll(Map<String, String> params, Authentication authentication) {
        String deviceId = params.containsKey("deviceId") ? params.get("deviceId").trim() : null;
        String deviceIds = null;
        if(deviceId == null) {
        	List<String> devices = deviceRepository.getCameraIds(authentication);
        	deviceIds = String.join(",", devices);
        }
        else deviceIds = deviceId.trim();
        
        
        List<ZoneEntity> zones =
                zoneRepository.searchZones(deviceIds);
       // System.out.println(zones);
        List<ZoneDto> zoneDtos = zones.stream().map(zone -> GsonUtils.mapObject(zone, ZoneDto.class)).collect(Collectors.toList());
     
        for(int i=0; i<zones.size(); i++) {
        	//System.out.println(zones.get(0).getCoords());
            List<ZonePointDto> zonePointDtos = convertStringToListPointDto(zones.get(i).getCoords());
            zoneDtos.get(i).setCoords(zonePointDtos);
            List<ZoneAttrEntity> zoneAttrs = zoneAttrRepository.findByZoneIdAndIsActiveIsTrue(zones.get(i).getId());
            zoneDtos.get(i).setZoneAttrs(GsonUtils.mapObject(zoneAttrs, new TypeToken<Set<ZoneAttrDto>>(){}.getType()));
        }
        return Response.builder()
                .code(HttpStatus.OK.value())
                .data(zoneDtos)
                .build();
    }


    @Override
    public BaseResponse updateOne(Integer id, ZoneInfoRequest zoneInfoRequest, Authentication authentication) {
        ZoneEntity zone = zoneRepository.findByIdAndIsActiveIsTrue(id);
        if(zone == null)
            throw new NotfoundException(WebConfig.translate("zone.not_found"));
        if(!PermissionUtils.hasAnyRole(authentication, Role.ADMIN) && !deviceRepository.isPermitted(zone.getDeviceId(), authentication))
        	throw new ForbiddenException();

        return null;
    }

    @Override
    public Response<?> deleteOne(Integer id, Authentication authentication) {
        ZoneEntity zone = zoneRepository.findByIdAndIsActiveIsTrue(id);
        Integer authId = (Integer) authentication.getPrincipal();
        if(zone == null)
            throw new NotfoundException(WebConfig.translate("zone.not_found"));
        if(!PermissionUtils.hasAnyRole(authentication, Role.ADMIN) && !deviceRepository.isPermitted(zone.getDeviceId(), authentication))
        	throw new ForbiddenException();
        
        zone.setIsActive(false);
        ObjectUtils.setModifyHistory(zone, authId);
        zoneRepository.save(zone);
        /**
         */
        restartScp(zone.getDeviceId(), authentication);
        return Response.builder()
                .code(HttpStatus.NO_CONTENT.value())
                .message(WebConfig.translate("process_success"))
                .build();
    }

    public void restartScp(String deviceId, Authentication authentication) {
        LOGGER.info("start scp");
        new Thread(() -> {
            startScp(deviceId, authentication);
        }).start();
    }

    private Gson gson = new Gson();
    @Value("${SCP_APIKEY}")
    private String SCP_APIKEY;

    private void startScp(String deviceId, Authentication authentication) {
    	//TODO add tnt id
        String tntId = "";

        List<ZoneEntity> zones = zoneRepository.findByDeviceIdAndIsActiveIsTrue(deviceId);
        Map<String, Object> scpConfig = new HashMap<>();

        List<ZoneTypeEntity> zoneTypes = zoneTypeRepository.findAll();
        Map<Integer, String> mapSettingCode = new HashMap<>();
        for(ZoneTypeEntity zoneType:zoneTypes)
            if(zoneType.getSettingCode() != null) mapSettingCode.put(zoneType.getId(), zoneType.getSettingCode());

        for(ZoneEntity zone:zones) {
            ScpArea area = new ScpArea();
            List<ZonePointDto> points = Arrays.asList(gson.fromJson(zone.getCoords(), ZonePointDto[].class));
            List<ScpPoint> scpPoints = new ArrayList<>();
            for(ZonePointDto point:points) {
                ScpPoint scpPoint = new ScpPoint(point.getX(), point.getY());
                scpPoints.add(scpPoint);
            }
            area.setPoly(scpPoints);

            List<ZoneAttrEntity> zoneAttrs = zoneAttrRepository.findByZoneIdAndIsActiveIsTrue(zone.getId());
            for(ZoneAttrEntity zoneAttr:zoneAttrs) {
                if(!mapSettingCode.containsKey(zoneAttr.getZoneTypeId())) continue;
                String settingCode = mapSettingCode.get(zoneAttr.getZoneTypeId());
                String[] paths = settingCode.split("\\.");

                if (!scpConfig.containsKey(paths[0]))
                    scpConfig.put(paths[0], new HashMap<String, Object>());
                Map<String, Object> violationMap = (Map<String, Object>) scpConfig.get(paths[0]);

                if(!violationMap.containsKey(paths[1]))
                    violationMap.put(paths[1], new ArrayList<ScpArea>());
                List<ScpArea> areas = (List<ScpArea>) violationMap.get(paths[1]);
                ScpArea scpArea = area.clone();
                scpArea.setAreaId(String.format("cam:%s_zone:%s_attr:%s", zone.getDeviceId(), zone.getId(), zoneAttr.getId()));
                scpArea.setValue(zoneAttr.getValue());
                areas.add(scpArea);
            }
        }

        ScpRequest scpRequest = new ScpRequest();
        scpRequest.setConfig(scpConfig);
        scpRequest.setCameraId(deviceId);
        scpRequest.setTenantId(tntId);
        scpRequest.setToken((String) authentication.getCredentials());
        
        LOGGER.info(new Gson().toJson(scpRequest));
        Object res = scpFeignClient.startService(scpRequest, SCP_APIKEY);
        LOGGER.info(new Gson().toJson(res));
    }

    @Override
    public BaseResponse stopScp(String deviceId) {
        ScpRequest scpRequest = new ScpRequest();
        scpRequest.setCameraId(deviceId);
        Object res = scpFeignClient.stopService(scpRequest, SCP_APIKEY);
        LOGGER.info(GsonUtils.parseToJson(res));
        return Response.builder()
                .data(res)
                .build();
    }

    @Override
    public BaseResponse stopAllScp() {
        Object res = scpFeignClient.stopAllService(SCP_APIKEY);
        LOGGER.info(new Gson().toJson(res));
        return Response.builder()
                .data(res)
                .build();
    }

    @Override
    public BaseResponse getZoneTypes() {
        List<ZoneTypeEntity> zoneTypes = zoneTypeRepository.findAll();
        List<ZoneViolationTypeDto> violationTypes =
                GsonUtils.mapObject(violationTypeRepository.findByIsActiveIsTrue(), new TypeToken<List<ZoneViolationTypeDto>>(){}.getType());
        violationTypes.forEach(violationType -> {
            List<ZoneTypeDto> zoneDtos = zoneTypes.stream()
                    .filter(zoneType -> violationType.getId().equals(zoneType.getViolationTypeId()))
                    .map(zoneType -> GsonUtils.mapObject(zoneType, ZoneTypeDto.class))
                    .collect(Collectors.toList());
            violationType.setZoneTypes(zoneDtos);
        });
        return Response.builder()
                .code(200)
                .data(violationTypes)
                .build();
    }

    private List<ZonePointDto> convertStringToListPointDto(String st) {
        return GsonUtils.parseJsonToObject(st, List.class);
    }
    

    private String convertAndCheckValidateListPoint(List<ZonePointRequest> points) {
        for(ZonePointRequest point:points) {
        	ObjectUtils.validate(point);
            if(point.getX() < 0 || point.getX() > 1 || point.getY() < 0 || point.getY() > 1)
                throw new BadRequestException(WebConfig.translate("zone.coords_not_valid"));
        }
        return new Gson().toJson(points);
    }

}
