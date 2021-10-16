package com.its.highway_service.repository;

import java.util.List;

import com.its.highway_service.model.dto.HighwaySearchDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.its.module.model.entity.HighwayEntity;

@Repository
public interface HighwayRepository extends JpaRepository<HighwayEntity, Integer> {
	HighwayEntity findByIdAndIsActiveIsTrue(Integer highwayId);

	HighwayEntity findByNameAndIsActiveIsTrue(String highwayName);

	@Query(value = "CALL its.proc_searchHighwaysAdmin(?,?,?,?);", nativeQuery = true)
	List<HighwaySearchDto> searchAdminRole(Integer userId, String name, int limit, int offset);

	@Query(value = "CALL its.proc_searchHighwaysUser(?,?,?,?);", nativeQuery = true)
	List<HighwaySearchDto> searchUserRole(Integer authId, String name, int limit, int offset);

	@Query(value = "SELECT func_isPermittedAccessHighway(?,?)", nativeQuery = true)
	int isPermittedAccessHighway(Integer authId, Integer highwayId);
}
