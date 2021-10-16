package com.smartcity.its.manager.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smartcity.its.manager.device.model.entity.ZoneEntity;
import com.smartcity.its.manager.device.model.entity.ZoneTypeEntity;

import java.util.List;

@Repository
public interface ZoneRepository extends JpaRepository<ZoneEntity, Integer> {
    ZoneEntity findByIdAndIsActiveIsTrue(Integer id);
    List<ZoneEntity> findByDeviceIdAndIsActiveIsTrue(String deviceId);

    @Query(value = "SELECT func_isPermittedAccessZone(:authId, :zoneId)", nativeQuery = true)
    int isPermittedAccessZone(Integer authId, Integer zoneId);

    @Query(value = "CALL proc_searchZones(?);", nativeQuery = true)
    List<ZoneEntity> searchZones(String deviceIds);
}
