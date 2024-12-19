package com.foodexpress.customer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.customer.dao.CustomerDao;
import com.foodexpress.customer.model.Customer;
import com.foodexpress.utilities.PasswordUtils;


@Service
public class CustomerService implements ICustomer{

	@Autowired
	CustomerDao customerDao;
	
	@Override
	public boolean registerCustomer(Customer customer) {
	    String email = customer.getEmail();

	    // Check if the email already exists
	    Optional<Customer> existingCustomer = customerDao.findByEmail(email);
	    if (existingCustomer.isPresent()) {
	        return false;
	    } else {
	        // Hash the password before saving
	        String hashedPassword = PasswordUtils.hashPassword(customer.getPassword());
	        customer.setPassword(hashedPassword);
	        customerDao.save(customer);
	        return true;
	    }
	}



	@Override
	public boolean updatePassword(String email, String password) {
	    // Fetch the customer by email
	    Optional<Customer> existingCustomer = customerDao.findByEmail(email);
	    
	    if (!existingCustomer.isPresent()) {
	        // If customer does not exist, return false
	        return false;
	    } else {
	        // Get the customer entity
	        Customer customer = existingCustomer.get();
	        
	        // Hash the new password
	        String hashedPassword = PasswordUtils.hashPassword(password);
	        
	        // Update the password field
	        customer.setPassword(hashedPassword);
	        
	        // Save the updated customer entity back to the database
	        customerDao.save(customer);
	        
	        return true;
	    }
	}


	@Override
	public boolean removeCustomer(int userId) {
		// TODO Auto-generated method stub
		if (customerDao.findById(userId).get() != null) {
			customerDao.deleteById(userId);
			return true;
		}
		return false;
	}

	@Override
	public boolean isExist(String email, String password) {
	    // Hash the password provided by the user
	    String hashedPassword = PasswordUtils.hashPassword(password);

	    // Find the customer by email and hashed password
	    Optional<Customer> customer = customerDao.findByEmailAndPassword(email, hashedPassword);

	    return customer.isPresent();
	}

}
