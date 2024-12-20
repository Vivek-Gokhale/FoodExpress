package com.foodexpress.customer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.customer.dao.AccountPreferenceDao;
import com.foodexpress.customer.model.AccountPreference;
import com.foodexpress.customer.model.Customer;

@Service
public class AccountPreferenceService implements IAccountPreference{

	
	
	@Autowired
	AccountPreferenceDao accountPreferenceDao;
	@Override
	public boolean updatePreference(AccountPreference accountPreference) {
		// TODO Auto-generated method stub
		if(accountPreferenceDao.save(accountPreference) != null)
		return true;
		return false;
	}
	
	
	@Override
	public AccountPreference getAccountPreference(int userId) {
		// TODO Auto-generated method stub
		Optional<AccountPreference> accountPreference = accountPreferenceDao.findByUserId(userId);
		return accountPreference.get();
	}
	
	

}
