package com.smartcity.its.manager.violation.repository;

import com.smartcity.its.manager.violation.model.entity.ViolationStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViolationStatusRepository extends JpaRepository<ViolationStatusEntity, Integer> {
}
