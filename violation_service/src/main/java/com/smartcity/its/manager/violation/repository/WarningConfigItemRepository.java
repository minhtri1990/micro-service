package com.smartcity.its.manager.violation.repository;

import com.smartcity.its.manager.violation.model.entity.WarningConfigItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarningConfigItemRepository extends JpaRepository<WarningConfigItemEntity, Integer> {
    List<WarningConfigItemEntity> findByIsActiveIsTrue();
    List<WarningConfigItemEntity> findByUserIdAndTypeAndIsActiveIsTrue(Integer userId, Integer type);
    WarningConfigItemEntity findByIdAndIsActiveIsTrue(Integer id);
    Integer countByUserIdAndViolationTypeIdAndIsActiveIsTrue(Integer userId, Integer violationTypeId);
    @Query(value = "select * from warning_config_item where user_id=:userId and violation_type_id=:violationTypeId and type=:type and is_active=1",
            nativeQuery = true)
    WarningConfigItemEntity findByUserIdAndViolationTypeIdAndIsActiveIsTrue(Integer userId, Integer type, Integer violationTypeId);
}
