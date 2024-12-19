package com.foodexpress.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodexpress.customer.model.Customer;
import com.foodexpress.customer.service.CustomerService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class CustomerController {

	@Autowired
	CustomerService customerService;
	
	@PostMapping("/register-customer")
	public String handleCustomerRegister(@ModelAttribute Customer customer ) {
		if(customerService.registerCustomer(customer))
		return "success";
		return "failed";
		
	}
	
	@PostMapping("/deactive-account/{userId}")
	public String handleAccountDeactivation(@PathVariable("userId") int id) {
		
		if(customerService.removeCustomer(id))
		return "success";
		return "failed";
		
	}
	
	@PostMapping("/login")
	public String handleLogin(@RequestBody String body) {
	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode jsonNode = objectMapper.readTree(body);

	        String email = jsonNode.get("email").asText();
	        String password = jsonNode.get("password").asText();

	        if (customerService.isExist(email, password)) {
	            return "success";
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return "failed";
	}
	
	
	@PostMapping("change-password")
	public String handlePasswordChange(@RequestBody String body)
	{
		try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode jsonNode = objectMapper.readTree(body);

	        String email = jsonNode.get("email").asText();
	        String password = jsonNode.get("password").asText();

	        if (customerService.updatePassword(email, password)) {
	            return "success";
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return "failed";
	}
	
}
