package com.foodexpress.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.foodexpress.customer.model.CustomerAddress;
import com.foodexpress.customer.service.CustomerAddressService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class CustomerAddressController {

    @Autowired
    private CustomerAddressService customerAddressService;

    @PostMapping("add-customer-address")
    public String addCustomerAddressHandle(@RequestBody String body) {
        try {
          
            ObjectMapper objectMapper = new ObjectMapper();
            CustomerAddress customerAddress = objectMapper.readValue(body, CustomerAddress.class);
            
            // Save the address
            boolean isSaved = customerAddressService.updateAddress(customerAddress);

            return isSaved ? "Address added successfully" : "Failed to add address";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing request: " + e.getMessage();
        }
    }
    
    @PostMapping("edit-customer-address")
    public String updateCustomerAddressHandle(@RequestBody String body) {
        try {
          
            ObjectMapper objectMapper = new ObjectMapper();
            CustomerAddress customerAddress = objectMapper.readValue(body, CustomerAddress.class);
            
            // Save the address
            boolean isSaved = customerAddressService.updateAddress(customerAddress);

            return isSaved ? "Address updated successfully" : "Failed to updated address";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing request: " + e.getMessage();
        }
    }
    
    @PostMapping("delete-customer-address/{aid}")
    public String deleteCustomerHandler(@PathVariable("aid") int aid)
    {
    	return customerAddressService.removeAddress(aid) ? "success" : "falied";
    }
    
    @PostMapping("set-default-address/{userId}/{aid}")
    public String setDefaultAddressHandle(@PathVariable int userId, @PathVariable int aid) {
        try {
            boolean isUpdated = customerAddressService.setDefaultAddress(userId, aid);

            // Return response based on the operation result
            return isUpdated ? "Default address updated successfully" : "Failed to update default address";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing request: " + e.getMessage();
        }
    }
    
    
}
