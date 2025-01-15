package com.foodexpress.customer.service;

import java.util.List;

import org.hibernate.sql.Update;

import com.foodexpress.customer.model.CustomerAddress;

public interface ICustomerAddress {
	/*
	 entered one address while registering that will be dafault
	 foto address and manager option add addresss, edit , delete , set deafult
	add open new interfae 
	delete remove address with aid 
	edit open interface with filled data and update based on aid
	
	 * */
	public List<CustomerAddress> getAddresses(int userId);
	public boolean updateAddress(CustomerAddress address);
	public boolean removeAddress(int aid);
	public boolean setDefaultAddress(int userId, int aid);
	
}
