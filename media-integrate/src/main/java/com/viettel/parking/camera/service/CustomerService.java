package com.viettel.parking.camera.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;

import com.viettel.parking.camera.data.model.AccountVMS;

public interface CustomerService {

	String login(String username, String password);

	Optional<User> findByToken(String token);

	AccountVMS findById(Long userId);
}
