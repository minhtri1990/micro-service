package com.viettel.parking.camera.service;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.viettel.parking.camera.data.model.AccountVMS;
import com.viettel.parking.camera.repository.AccountVMSRepository;

@Service("customerService")
public class DefaultCustomerService implements CustomerService {

	@Autowired
	private AccountVMSRepository accountVMSRepository;

	@Override
	public String login(String username, String password) {
		AccountVMS customer = accountVMSRepository.findByUsernameAndKeypwd(username, password);
		if (customer != null) {
			String token = UUID.randomUUID().toString();
			customer.setTokenApi(token);
			accountVMSRepository.save(customer);
			return token;
		}

		return StringUtils.EMPTY;
	}

	@Override
	public Optional<User> findByToken(String tokenApi) {
		AccountVMS customer = accountVMSRepository.findByTokenApi(tokenApi);
		if (customer != null) {
			User user = new User(customer.getUsername(), customer.getKeypwd(), true, true, true, true,
					AuthorityUtils.createAuthorityList("USER"));
			return Optional.of(user);
		}
		return Optional.empty();
	}

	@Override
	public AccountVMS findById(Long userId) {
		AccountVMS customer = accountVMSRepository.findByUserId(userId);
		if (customer != null) {
			return customer;
		}
		return null;
	}

}
