package com.foodexpress.customer.service;

import com.foodexpress.customer.model.AccountPreference;

public interface IAccountPreference {
	public boolean updatePreference(AccountPreference accountPreference);
	public AccountPreference getAccountPreference(int userId);
	
}
