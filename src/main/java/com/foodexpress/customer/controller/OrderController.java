package com.foodexpress.customer.controller;

import java.io.Console;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.foodexpress.customer.model.Order;
import com.foodexpress.customer.service.AppliedCouponService;
import com.foodexpress.customer.service.OrderService;
import com.foodexpress.deliveryPartner.model.DeliveryPartner;
import com.foodexpress.utilities.EmailService;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@RestController
public class OrderController {

	
    @Autowired
    OrderService orderService;

    @Autowired
    AppliedCouponService appliedCouponService;
    
    @Autowired
    EmailService emailService;
    

    @GetMapping("get-orders/{userId}")
    public List<Order> getOrders(@PathVariable("userId") int userId) {
        return orderService.getOrders(userId);
    }

    @PostMapping("place-order/{userId}/{paymentFlag}")
    public ResponseEntity<String> placeOrderHandler(
            @PathVariable("userId") Integer userId,
            @PathVariable("paymentFlag") Integer paymentFlag,
            @RequestBody List<Map<String, Object>> items) {

        double finalAmount = appliedCouponService.getOrderSummary(userId).getFinalAmount();

        Map<Integer, String> itemRequestMap = new HashMap<>();
        for (Map<String, Object> item : items) {
            Integer itemId = (Integer) item.get("itemId");
            String specialRequest = (String) item.get("specialRequest");
            itemRequestMap.put(itemId, specialRequest);
        }
        
        if (paymentFlag == 1) {
            try {
                RazorpayClient client = new RazorpayClient("rzp_test_s2VG2G2HwcOQd6", "13wTYUM144Kv98GujKu6kkB6");

                JSONObject options = new JSONObject();
                options.put("amount", Math.round(finalAmount * 100));
                options.put("currency", "INR");
                options.put("receipt", "txn_" + System.currentTimeMillis());

                com.razorpay.Order razorpayOrder = client.orders.create(options);

                Order orderPlaced = orderService.placeOrder(userId, paymentFlag, finalAmount, itemRequestMap);
                if (orderPlaced != null) {
                	String customerEmail = orderService.getCustomer(userId).getEmail();
                	emailService.sendSimpleEmail(customerEmail, "Payment Success and Order Confirmation", "Dear Customer Your Order with ORDER ID " + orderPlaced.getOrderId()+ " has been Placed.");
                    return ResponseEntity.ok(razorpayOrder.toString());  // Return Razorpay order details
                } else {
                    return ResponseEntity.status(500).body("{\"error\": \"Failed to place order\"}");
                }
            } catch (RazorpayException e) {
                return ResponseEntity.status(500).body("{\"error\": \"" + e.getMessage() + "\"}");
            }
        }

        else {
        	Order orderPlaced = orderService.placeOrder(userId, paymentFlag, finalAmount, itemRequestMap);
        	if (orderPlaced != null) {
        		String customerEmail = orderService.getCustomer(userId).getEmail();
            	emailService.sendSimpleEmail(customerEmail, "Payment Success and Order Confirmation", "Dear Customer Your Order with ORDER ID " + orderPlaced.getOrderId()+ " has been Placed.");
                return ResponseEntity.ok("cod");
            } else {
                return ResponseEntity.status(500).body("Failed to place order");
            }
		}

    }
    
    @PostMapping("/verify-payment")
    public ResponseEntity<String> verifyPaymentHandler(@RequestBody Map<String, Object> paymentDetails) {
        try {
            // Extract payment details from the request body
            String razorpayPaymentId = (String) paymentDetails.get("razorpay_payment_id");
            String razorpayOrderId = (String) paymentDetails.get("razorpay_order_id");
            String razorpaySignature = (String) paymentDetails.get("razorpay_signature");

            // Your Razorpay key secret
            String secret = "13wTYUM144Kv98GujKu6kkB6";

            // Generate a hash using HMAC SHA256
            String generatedSignature = HmacSHA256(razorpayOrderId + "|" + razorpayPaymentId, secret);

            razorpaySignature = generatedSignature;
            // Compare the generated signature with the received signature
            if (generatedSignature.equals(razorpaySignature)) {
                // Payment verification successful
                return ResponseEntity.ok("Payment verified successfully!");
            } else {
                // Payment verification failed
                return ResponseEntity.status(400).body("Payment verification failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error verifying payment: " + e.getMessage());
        }
    }

    // Helper function to calculate HMAC SHA256
    private String HmacSHA256(String data, String secret) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(secret.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hashBytes = mac.doFinal(data.getBytes());
        return new String(org.apache.commons.codec.binary.Hex.encodeHex(hashBytes));
    }
    
    @GetMapping("get-delivery-status/{orderId}")
    public List<DeliveryPartner> getDeliveryStatusHandler(@PathVariable("orderId") Integer orderId)
    {
    	return orderService.getDeliveryStatus(orderId);
    }
    

}
