package com.foodexpress.customer.controller;

import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodexpress.customer.model.Customer;
import com.foodexpress.customer.service.CustomerService;
import com.foodexpress.utilities.EmailService;
import com.foodexpress.utilities.OTPGenerator;

import okhttp3.ResponseBody;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpSession;


@RestController
public class CustomerController {

	@Autowired
	CustomerService customerService;
	
	@Autowired
	EmailService emailService;
	
	
	@PostMapping("register-customer")
    public ResponseEntity<String> generateOTPHandler(@RequestBody Customer customer,  HttpSession session) {
        String otp = OTPGenerator.generateOTP();
        // You can store the OTP in the database or send it via email/SMS
        session.setAttribute("otp", otp);
        emailService.sendSimpleEmail(customer.getEmail(), "Registration OTP", "Dear " + customer.getFirstName() + " use OTP " + otp + " to verify your email");
        
        return ResponseEntity.ok(otp);
    }
	
	 @PostMapping("verify-registration-otp")
	    public ResponseEntity<String> handleCustomerRegister(@RequestBody String customerJson, HttpSession session) {
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            Customer customer = objectMapper.readValue(customerJson, Customer.class);
	            
	            String otpFromRequest = customer.getOtp();  	            
	            
	            String otpFromSession = (String) session.getAttribute("otp");
	            
	            // Verify OTP
	            if (otpFromSession != null && otpFromSession.equals(otpFromRequest)) {
	                
	                if (customerService.registerCustomer(customer)) {
	                    session.removeAttribute("otp");  // Clear OTP from session after successful verification
	                    return ResponseEntity.ok("Registration successful");
	                }
	                return ResponseEntity.status(400).body("Registration failed");
	            } else {
	                return ResponseEntity.status(400).body("Invalid OTP");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseEntity.status(500).body("Internal server error");
	        }
	    }

	
	@PostMapping("/deactive-account/{userId}")
	public String handleAccountDeactivation(@PathVariable("userId") int id) {
		
		if(customerService.removeCustomer(id))
		return "success";
		return "failed";
		
	}
	
	
	@PostMapping("/verify-login")
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
	
	@PostMapping("send-password-reset-otp")
	public ResponseEntity<String> generateOTPHandler2(@RequestBody String body, HttpSession session) {
	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode jsonNode = objectMapper.readTree(body);

	        String email = jsonNode.get("email").asText();

	        String otp = OTPGenerator.generateOTP();

	        session.setAttribute("otp", otp);

	        emailService.sendSimpleEmail(email, "Password Reset OTP", 
	            "Dear customer, use OTP " + otp + " to reset your password");

	        return ResponseEntity.ok("OTP sent successfully.");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Failed to generate OTP.");
	    }
	}

	
	@PostMapping("verify-password-reset-otp")
	public ResponseEntity<String> handlePasswordChangeVerify(@RequestBody String body, HttpSession session) {
	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode jsonNode = objectMapper.readTree(body);

	        String otpFromRequest = jsonNode.get("otp").asText();

	        String otpFromSession = (String) session.getAttribute("otp");

	        if (otpFromSession != null && otpFromSession.equals(otpFromRequest)) {
	            return ResponseEntity.ok("OTP verified");
	        } else {
	            return ResponseEntity.status(400).body("Invalid OTP");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Failed to verify OTP");
	    }
	}

	
	@PostMapping("reset-password")
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
