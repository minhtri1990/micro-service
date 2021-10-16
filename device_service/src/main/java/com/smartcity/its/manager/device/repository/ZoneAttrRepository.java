package com.smartcity.its.manager.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smartcity.its.manager.device.model.entity.ZoneAttrEntity;

import java.util.List;

@Repository
public interface ZoneAttrRepository extends JpaRepository<ZoneAttrEntity, Integer> {
    List<ZoneAttrEntity> findByZoneTypeIdAndIsActiveIsTrue(Integer zoneTypeId);
    List<ZoneAttrEntity> findByZoneIdAndIsActiveIsTrue(Integer zoneId);
}
