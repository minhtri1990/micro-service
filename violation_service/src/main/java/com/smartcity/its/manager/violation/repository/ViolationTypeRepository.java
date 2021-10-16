package com.smartcity.its.manager.violation.repository;

import com.its.module.model.entity.ViolationTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViolationTypeRepository extends JpaRepository<ViolationTypeEntity, Integer> {
    List<ViolationTypeEntity> findByTypeAndIsActiveIsTrue(Integer type);
    List<ViolationTypeEntity> findByIsActiveIsTrue();
    ViolationTypeEntity findByIdAndIsActiveIsTrue(Integer id);
    ViolationTypeEntity findByCodeAndIsActiveIsTrue(String code);
    @Query(value = "SELECT * FROM violation_type WHERE violation_code=:violationCode AND is_active=1;", nativeQuery = true)
    ViolationTypeEntity findByViolationCodeAndIsActiveIsTrue(String violationCode);
}
