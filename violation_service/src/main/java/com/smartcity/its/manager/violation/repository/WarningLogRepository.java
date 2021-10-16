package com.smartcity.its.manager.violation.repository;

import com.smartcity.its.manager.violation.model.entity.WarningLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarningLogRepository extends JpaRepository<WarningLogEntity, Integer> {
    int countByViolationId(Integer violationId);
}
