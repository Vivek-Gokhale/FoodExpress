package com.foodexpress.deliveryPartner.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.foodexpress.customer.model.Customer;
import com.foodexpress.customer.service.CustomerService;
import com.foodexpress.deliveryPartner.dto.OrderStatus;
import com.foodexpress.deliveryPartner.model.DeliveryPartner;
import com.foodexpress.deliveryPartner.service.DeliveryOrderService;
import com.foodexpress.utilities.EmailService;
import com.foodexpress.utilities.OTPGenerator;

import jakarta.servlet.http.HttpSession;

@RestController
public class DeliveryOrderController {

	@Autowired
	private DeliveryOrderService deliveryOrderService;
	
	@Autowired
	private EmailService emailService;
	
	
	@PostMapping("select-delivery-order")
	public boolean selectDeliveryOrderHandler(@RequestBody Map<String, Object> body)
	{
		Integer partnerId = (Integer) body.get("partnerId");
	    Integer orderId = (Integer) body.get("orderId");
	    String longitude = (String) body.get("longitude");
	    String latitude = (String) body.get("latitude");

	    // Call the service method with extracted parameters
	    if(deliveryOrderService.selectDeliveryOrder(partnerId, orderId, longitude, latitude))
	    {
	    	String customerEmail = deliveryOrderService.getCustomerByOrderId(orderId).getEmail();
	  	    emailService.sendSimpleEmail(customerEmail, "Order Shipped", "Your Order with ORDER ID " + orderId + " has been shipped");
	  	    return true;
	    }
	    return false;
	}
	
	@PostMapping("send-delivery-otp/{userId}")
	public ResponseEntity<String> generateOTPHandler(@PathVariable("userId") Integer userId, HttpSession session) {
	    try {
	      
	        String email = deliveryOrderService.getCustomer(userId).getEmail();

	        String otp = OTPGenerator.generateOTP();

	        session.setAttribute("otp", otp);
	        System.out.println(session.getAttribute("otp"));

	        emailService.sendSimpleEmail(email, "Delivery Order OTP", 
	            "Dear Customer, use this OTP " + otp + " to confirm delivery.");

	        return ResponseEntity.ok("OTP sent successfully");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Failed to send OTP. Please try again.");
	    }
	}

	
	@PutMapping("verify-otp/{deliveryId}/{userId}/{otp}/{orderId}")
	public ResponseEntity<String> verifyOTPHandler(
	    @PathVariable("deliveryId") Integer deliveryId,
	    @PathVariable("userId") Integer userId,
	    @PathVariable("otp") String otp,
	    @PathVariable("orderId") String orderId, // Use @RequestParam for OTP input
	    HttpSession session) {
	    try {
	        
	        String otpFromSession = (String) session.getAttribute("otp");
	        System.out.println(otpFromSession);
	        // Verify the OTP
	        if (otpFromSession != null && otpFromSession.equals(otp)) {
	        	if(deliveryOrderService.verifyOTP(deliveryId, userId))
	        	{
	        		System.out.println("called");
	        		String customerEmail = deliveryOrderService.getCustomer(userId).getEmail();
	    	  	    emailService.sendSimpleEmail(customerEmail, "Order Delivered", "Your Order with ORDER ID " + orderId + " has been Delivered");
	    	  	    return ResponseEntity.ok("order delivered");
	        	}
	            
	        	else return ResponseEntity.status(400).body("Falied to deliver");
	        } else {
	            return ResponseEntity.status(400).body("Invalid OTP");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body("Failed to verify OTP. Please try again.");
	    }
	}

	
	@GetMapping("get-partner-pending-orders/{partnerId}")
	public List<OrderStatus> getPendingDeliveryOrderHandler(@PathVariable("partnerId") Integer partnerId)
	{
		return deliveryOrderService.getPendingOrders(partnerId);	
	}
	
	@GetMapping("get-partner-completed-orders/{partnerId}")
	public List<OrderStatus> getCompletedDeliveryOrderHandler(@PathVariable("partnerId") Integer partnerId)
	{
		return deliveryOrderService.getCompletedOrders(partnerId);	
	}
	
	@GetMapping("/check-live-tracking-con/{orderId}")
	public ResponseEntity<String> checkLiveTrackingConditionHandler(@PathVariable("orderId") int orderId) {
	    boolean flag = deliveryOrderService.checkLiveTrackingCondition(orderId);
	    if (flag) {
	        return ResponseEntity.ok("Live tracking is enabled for this order.");
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                             .body("Live tracking is not available for this order.");
	    }
	}
	
	@GetMapping("get-delivery-partner/{orderId}")
	public ResponseEntity<Object[]> getDeliveryPartnerHandler(@PathVariable("orderId") int orderId) {
	    Object[] partner = deliveryOrderService.getDeliveryPartner(orderId);
	    if (partner != null) {
	        return ResponseEntity.ok(partner);  // Return 200 OK with the partner object
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Return 404 Not Found if partner is null
	    }
	}


}
