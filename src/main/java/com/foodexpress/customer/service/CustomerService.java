package com.foodexpress.customer.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodexpress.customer.dao.AccountPreferenceDao;
import com.foodexpress.customer.dao.CustomerDao;
import com.foodexpress.customer.model.AccountPreference;
import com.foodexpress.customer.model.Customer;
import com.foodexpress.utilities.PasswordUtils;


@Service
public class CustomerService implements ICustomer{

	@Autowired
	CustomerDao customerDao;
	
	 @Autowired
     AccountPreferenceDao accountPreferenceDao;
	
	 @Override
	 public boolean registerCustomer(Customer customer) {
	     String email = customer.getEmail();

	     // Check if the email already exists
	     Optional<Customer> existingCustomer = customerDao.findByEmail(email);
	     if (existingCustomer.isPresent()) {
	         return false; // Registration failed due to duplicate email
	     } else {
	         // Hash the password before saving
	         String hashedPassword = PasswordUtils.hashPassword(customer.getPassword());
	         customer.setPassword(hashedPassword);

	         // Save the customer
	         Customer savedCustomer = customerDao.save(customer);

	         // Create default account preferences
	         AccountPreference accountPreference = AccountPreference.builder()
	                 .userId(savedCustomer.getUserId()) // Set userId from saved customer
	                 .notificationFlag(true)
	                 .newsletterFlag(true)
	                 .promosAndOfferFlag(true)
	                 .orderStatusFlag(true)
	                 .coins(100)
	                 .build();

	         // Save the account preferences
	         accountPreferenceDao.save(accountPreference);

	         return true; // Registration successful
	     }
	 }




	@Override
	public boolean updatePassword(String email, String password) {
	    // Fetch the customer by email
	    Optional<Customer> existingCustomer = customerDao.findByEmail(email);
	    
	    if (!existingCustomer.isPresent()) {
	    	System.out.println("not exist");
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
	public Customer isExist(String email, String password) {
	    // Hash thepassword provided by the user
	    String hashedPassword = PasswordUtils.hashPassword(password);

	    // Find the customer by email and hashed password
	    Optional<Customer> customer = customerDao.findByEmailAndPassword(email, hashedPassword);

	    return customer.get();
	}

}
