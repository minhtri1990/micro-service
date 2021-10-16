package com.smartcity.its.manager.device.repository;

import com.its.module.model.entity.ViolationTypeEntity;
import com.smartcity.its.manager.device.model.entity.ZoneTypeEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZoneTypeRepository extends JpaRepository<ZoneTypeEntity, Integer> {
}
