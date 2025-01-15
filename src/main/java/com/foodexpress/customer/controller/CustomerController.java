package com.foodexpress.customer.controller;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	public ResponseEntity<Map<String, String>> handleCustomerRegister(@RequestBody String customerJson, HttpSession session) {
	    Map<String, String> response = new HashMap<>();
	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        Customer customer = objectMapper.readValue(customerJson, Customer.class);
	        
	        String otpFromRequest = customer.getOtp();
	        String otpFromSession = (String) session.getAttribute("otp");
	        
	        // Verify OTP
	        if (otpFromSession != null && otpFromSession.equals(otpFromRequest)) {
	            if (customerService.registerCustomer(customer)) {
	                session.removeAttribute("otp"); // Clear OTP from session
	                response.put("message", "Registration successful");
	                return ResponseEntity.ok(response);
	            } else {
	                response.put("error", "User with this email already exists");
	                return ResponseEntity.status(400).body(response);
	            }
	        } else {
	            response.put("error", "Invalid OTP");
	            return ResponseEntity.status(400).body(response);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("error", "Internal server error");
	        return ResponseEntity.status(500).body(response);
	    }
	}


	
	@PostMapping("/deactive-account/{userId}")
	public String handleAccountDeactivation(@PathVariable("userId") int id) {
		
		if(customerService.removeCustomer(id))
		return "success";
		return "failed";
		
	}
	
	
	@PostMapping("verify-customer-login")
	public ResponseEntity<Map<String, String>> handleLogin(@RequestBody Map<String, String> request) {
	    Map<String, String> response = new HashMap<>();
	    try {
	        String email = request.get("email");
	        String password = request.get("password");

	        Customer customer = customerService.isExist(email, password);
	        if (customer != null) {
	            response.put("username", customer.getFirstName());
	            response.put("userId", String.valueOf(customer.getUserId()));
	            response.put("redirectTo", "index"); // Set the desired redirect URL
	            return ResponseEntity.ok(response);
	        }
	        response.put("status", "failed");
	        response.put("error", "Invalid email or password");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", "error");
	        response.put("error", "An internal server error occurred");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
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
	public ResponseEntity<String> handlePasswordChange(@RequestBody String body) {
	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        JsonNode jsonNode = objectMapper.readTree(body);

	        String email = jsonNode.get("email").asText();
	        String password = jsonNode.get("password").asText();

	        if (customerService.updatePassword(email, password)) {
	            return ResponseEntity.ok("Password reset successfully.");
	        } else {
	            return ResponseEntity.status(400).body("Failed to reset password. Please check the details.");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("An error occurred while processing your request.");
	    }
	}
	
}
