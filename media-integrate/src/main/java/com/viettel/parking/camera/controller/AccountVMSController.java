package com.viettel.parking.camera.controller;

import java.util.List;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.viettel.parking.camera.data.model.AccountVMS;
import com.viettel.parking.camera.network.reponse.BaseResponse;
import com.viettel.parking.camera.repository.AccountVMSRepository;
import com.viettel.parking.camera.service.AccountService;

@RestController
@RequestMapping("/api/account")
public class AccountVMSController {

	@Autowired
	private AccountVMSRepository accountVMSRepository;

	@Autowired
	private AccountService accountService;

	/**
	 * create account
	 * 
	 * @param account
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public BaseResponse<AccountVMS> createAccount(@Valid @RequestBody AccountVMS account) {
		return accountService.createAccount(account);
	}

	/**
	 * get All Account
	 * 
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public BaseResponse<List<AccountVMS>> getAllAccount() {
		return accountService.findAll();
	}

	/**
	 * get one
	 * 
	 * @return
	 */
	@RequestMapping(value = "/top", method = RequestMethod.GET)
	public AccountVMS getTopAccount() {
		List<AccountVMS> list = accountVMSRepository.findAll();
		if (list == null || list.size() == 0) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * get by id
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public AccountVMS getAccounttById(@PathVariable("id") ObjectId id) {
		return accountVMSRepository.findBy_id(id);
	}

	/**
	 * create account
	 * 
	 * @param account
	 * @return
	 */
	@RequestMapping(value = "/find", method = RequestMethod.POST)
	public BaseResponse<AccountVMS> getAccounttByName(@Valid @RequestBody AccountVMS account) {
		return accountService.findByName(account);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
	public void modifyAccountById(@PathVariable("id") ObjectId id, @Valid @RequestBody AccountVMS account) {
		account.set_id(id);
		accountVMSRepository.save(account);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public void deleteAccount(@PathVariable ObjectId id) {
		accountVMSRepository.delete(accountVMSRepository.findBy_id(id));
	}
}
