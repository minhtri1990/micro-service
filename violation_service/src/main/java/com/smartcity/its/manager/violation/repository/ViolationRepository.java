package com.smartcity.its.manager.violation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.smartcity.its.manager.violation.model.entity.ViolationEntity;

@Repository
public interface ViolationRepository extends JpaRepository<ViolationEntity, String>{
	ViolationEntity findByIdAndIsActiveIsTrue(String id);
	
	@Query(value = "CALL proc_searchViolations(?,?,?,?,?,?,?);", nativeQuery = true)
	List<ViolationEntity> searchViolations(String deviceIds, String licensePlate, Integer status, Long startTime, Long endTime, Integer limit, Integer offset);
	
	@Query(value = "SELECT func_countSearchViolations(?,?,?,?,?);", nativeQuery = true)
	Integer countSearchViolations(String deviceIds, String licensePlate, Integer status, Long startTime, Long endTime);
}
