package com.its.highway_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.its.module.model.entity.UserHighwayEntity;

@Repository
public interface UserHighwayRepository extends JpaRepository<UserHighwayEntity, Integer> {
	@Query(value = "SELECT checkUserHighwayValid(:userId, :highwayId);", nativeQuery = true)
	Integer checkUserHighway(Integer userId, Integer highwayId);

	Integer countByUserIdAndHighwayIdAndIsActiveIsTrue(Integer userId, Integer highwayId);

	UserHighwayEntity findByUserIdAndHighwayIdAndIsActiveIsTrue(Integer userId, Integer highwayId);

	List<UserHighwayEntity> findByIsActiveIsTrue();

	List<UserHighwayEntity> findByUserIdAndIsActiveIsTrue(Integer userId);

	@Query(value = "select count(*) from its.user where user_id=:userId and is_active=1", nativeQuery = true)
	Integer countUserById(Integer userId);
}
