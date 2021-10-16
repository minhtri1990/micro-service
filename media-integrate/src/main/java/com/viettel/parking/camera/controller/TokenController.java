package com.viettel.parking.camera.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.viettel.parking.camera.data.model.AccountVMS;
import com.viettel.parking.camera.network.reponse.BaseResponse;
import com.viettel.parking.camera.service.AccountService;
import com.viettel.parking.camera.service.CustomerService;
import com.viettel.parking.camera.utils.EncodeUtil;

@RestController
public class TokenController {
	@Autowired
	private CustomerService customerService;

	@Autowired
	private AccountService accountService;

	@PostMapping("/token/validate")
	public String getToken(@RequestParam("username") final String username,
			@RequestParam("password") final String password) {
		String keypwd = EncodeUtil.sha256hex(password);
		String token = customerService.login(username, keypwd);
		if (StringUtils.isEmpty(token)) {
			AccountVMS account = new AccountVMS();
			account.setUsername(username);
			account.setKeypwd(password);
			BaseResponse<AccountVMS> res = accountService.createAccount(account);
			if (res.getData() == null) {
				return "not found";
			}
			return res.getData().getToken();
		}
		return token;
	}

}
