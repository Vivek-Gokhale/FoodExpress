package com.foodexpress.customer.controller;

import java.io.Console;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<String> addCustomerAddressHandle(@RequestBody CustomerAddress customerAddress) {
        try {
           

            // Save the address
            boolean isSaved = customerAddressService.updateAddress(customerAddress);

            // Return a success response if saved, or failure if not
            if (isSaved) {
                return ResponseEntity.ok("{\"success\": true, \"message\": \"Address added successfully\"}");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"Failed to add address\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"success\": false, \"message\": \"Error processing request: " + e.getMessage() + "\"}");
        }
    }

    @GetMapping("get-customer-address/{aid}")
    public ResponseEntity<CustomerAddress> getCustomerAddressHandle(@PathVariable("aid") int aid) {
        CustomerAddress customerAddress = customerAddressService.getCustomerAddress(aid);
        return ResponseEntity.ok(customerAddress);
    }
    
    @GetMapping("get-customer-addresses/{userId}")
    public ResponseEntity<List<CustomerAddress>> getCustomerAddressesHandle(@PathVariable("userId") int userId)
    {
    	List<CustomerAddress> customerAddresses = customerAddressService.getAddresses(userId);
    	return ResponseEntity.ok(customerAddresses);
    }

    @PostMapping("edit-customer-address/{isDefault}")
    public ResponseEntity<String> updateCustomerAddressHandle(@RequestBody CustomerAddress customerAddress, @PathVariable("isDefault") boolean isDefault) {
    	customerAddress.setDefault(isDefault);
    	System.out.println(customerAddress);
        try {
            // Save the address
            boolean isSaved = customerAddressService.updateAddress(customerAddress);

            if (isSaved) {
                return ResponseEntity.ok("{\"success\": true, \"message\": \"Address updated successfully\"}");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"Failed to update address\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"success\": false, \"message\": \"Error processing request: " + e.getMessage() + "\"}");
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
