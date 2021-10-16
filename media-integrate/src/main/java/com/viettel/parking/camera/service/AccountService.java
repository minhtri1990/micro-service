package com.viettel.parking.camera.service;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.viettel.parking.camera.data.model.AccountVMS;
import com.viettel.parking.camera.network.VmsApi;
import com.viettel.parking.camera.network.reponse.BaseResponse;
import com.viettel.parking.camera.network.reponse.LoginVMSResponse;
import com.viettel.parking.camera.repository.AccountVMSRepository;
import com.viettel.parking.camera.utils.EncodeUtil;

@Service
public class AccountService extends BaseService {

	@Autowired
	private AccountVMSRepository accountVMSRepository;

	@Autowired
	private VmsApi vmsApi;

	/**
	 * create account
	 * 
	 * @param account
	 * @return
	 */
	public BaseResponse<AccountVMS> createAccount(AccountVMS account) {
		// check duplicate
		AccountVMS data = accountVMSRepository.findByUsername(account.getUsername());
		BaseResponse<AccountVMS> response = new BaseResponse<AccountVMS>();
		if (data != null) {
			response.setSuccess();
			response.setData(data);
			return response;
		}

		// validate account
		LoginVMSResponse loginResponse = vmsApi.login(account);
		if (loginResponse.isSuccess()) {
			account.set_id(ObjectId.get());
			account.setUserId(loginResponse.getUser_id());
			account.setFullname(loginResponse.getFullname());
			account.setKeypwd(EncodeUtil.sha256hex(account.getKeypwd()));
			account.setAddress(loginResponse.getAddress());
			account.setEmail(loginResponse.getEmail());
			account.setRoleAdmin(loginResponse.getRole_admin());
			account.setToken(loginResponse.getToken());
			account.setTokenApi(UUID.randomUUID().toString());
			account.setSite(loginResponse.getSite());
			accountVMSRepository.save(account);
			response.setSuccess();
			response.setData(account);
			return response;
		} else {
			response.setError();
			return response;
		}
	}

	/**
	 * reload Token
	 * 
	 * @param storeAccount
	 * @return
	 */
	public AccountVMS reloadToken(AccountVMS storeAccount) {
		// validate account
		LoginVMSResponse loginResponse = vmsApi.reLogin(storeAccount);
		if (loginResponse.isSuccess()) {
			storeAccount.set_id(storeAccount.getObjId());
			storeAccount.setAddress(loginResponse.getAddress());
			storeAccount.setEmail(loginResponse.getEmail());
			storeAccount.setRoleAdmin(loginResponse.getRole_admin());
			storeAccount.setToken(loginResponse.getToken());
			storeAccount.setSite(loginResponse.getSite());
			accountVMSRepository.save(storeAccount);
			return storeAccount;
		} else {
			return storeAccount;
		}
	}

	/**
	 * get all account
	 * 
	 * @return
	 */
	public BaseResponse<List<AccountVMS>> findAll() {
		List<AccountVMS> listAccount = accountVMSRepository.findAll();
		BaseResponse<List<AccountVMS>> response = new BaseResponse<List<AccountVMS>>();
		response.setSuccess();
		response.setData(listAccount);
		return response;
	}

	/**
	 * find by name
	 * 
	 * @param account
	 * @return
	 */
	public BaseResponse<AccountVMS> findByName(@Valid AccountVMS account) {
		BaseResponse<AccountVMS> response = new BaseResponse<AccountVMS>();
		AccountVMS data = accountVMSRepository.findByUsername(account.getUsername());
		response.setSuccess();
		response.setData(data);
		return response;
	}
}
