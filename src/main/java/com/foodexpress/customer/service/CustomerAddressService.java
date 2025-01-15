package com.foodexpress.customer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.foodexpress.customer.dao.CustomerAddressDao;
import com.foodexpress.customer.model.CustomerAddress;

@Service
public class CustomerAddressService implements ICustomerAddress {

    @Autowired
    private CustomerAddressDao customerAddressDao;

    @Override
    public List<CustomerAddress> getAddresses(int userId) {
        return customerAddressDao.findAllByUserId(userId);
    }

    @Override
    public boolean updateAddress(CustomerAddress address) {
        return customerAddressDao.save(address) != null;
    }

    @Override
    public boolean removeAddress(int aid) {
        if (customerAddressDao.existsById(aid)) {
            customerAddressDao.deleteById(aid);
            return true;
        }
        return false;
    }

    @Override
    public boolean setDefaultAddress(int userId, int aid) {
        Optional<CustomerAddress> optionalAddress = customerAddressDao.findById(aid);
        if (optionalAddress.isPresent() && optionalAddress.get().getUserId() == userId) {
            List<CustomerAddress> userAddresses = customerAddressDao.findAllByUserId(userId);
            for (CustomerAddress address : userAddresses) {
                address.setDefault(address.getAid() == aid);
            }
            customerAddressDao.saveAll(userAddresses);
            return true;
        }
        return false;
    }

	public CustomerAddress getCustomerAddress(int aid) {
		// TODO Auto-generated method stub
		return customerAddressDao.findById(aid).get();
	}
}
