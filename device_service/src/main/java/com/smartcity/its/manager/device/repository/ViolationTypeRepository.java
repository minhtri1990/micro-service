package com.smartcity.its.manager.device.repository;

import com.its.module.model.entity.ViolationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViolationTypeRepository extends JpaRepository<ViolationTypeEntity, Integer> {
    List<ViolationTypeEntity> findByIsActiveIsTrue();
    ViolationTypeEntity findByIdAndIsActiveIsTrue(Integer id);
}
