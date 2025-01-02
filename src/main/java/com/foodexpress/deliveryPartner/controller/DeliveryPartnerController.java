package com.foodexpress.deliveryPartner.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.foodexpress.deliveryPartner.dto.PendingOrderStatus;
import com.foodexpress.deliveryPartner.model.DeliveryPartner;
import com.foodexpress.deliveryPartner.service.DeliveryOrderService;
import com.foodexpress.deliveryPartner.service.DeliveryPartnerService;
import com.foodexpress.utilities.FileUploadHelper;

@RestController
public class DeliveryPartnerController {

	
		@Autowired
		private DeliveryPartnerService deliveryPartnerService;
	
		@PutMapping("update-location")
		public boolean updateLocationHandler(@RequestBody Map<String, Object> body)
		{
			Integer partnerId = (Integer) body.get("partnerId");
		    String longitude = (String) body.get("longitude");
		    String latitude = (String) body.get("latitude");
		    return deliveryPartnerService.updateLocation(partnerId, longitude, latitude);
		}
		
		@PostMapping("register-delivery-partner")
		public ResponseEntity<?> registerDeliveryPartner(
		    @RequestParam("name") String name,
		    @RequestParam("email") String email,
		    @RequestParam("password") String password,
		    @RequestParam("servingPincode") String servingPincode,
		    @RequestParam("phoneNo") String phoneNo,
		    @RequestParam("currLocLongitude") String currLocLongitude,
		    @RequestParam("currLocLatitude") String currLocLatitude,
		    @RequestParam("identityProof") MultipartFile file) {

			
		    // Validate file
		    if (file == null || file.isEmpty()) {
		        return ResponseEntity.badRequest().body("Identity proof file is required.");
		    }
		    String contentType = file.getContentType();
		    if (contentType == null || !contentType.startsWith("image/")) {
		        return ResponseEntity.badRequest().body("Only image files are allowed for identity proof.");
		    }

		    // Upload file
		    String uploadedFileName;
		    try {
		        uploadedFileName = FileUploadHelper.uploadFile(file);
		        
		    } catch (Exception e) {
		        e.printStackTrace();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed.");
		    }

		    // Create DeliveryPartner object
		    DeliveryPartner partner = new DeliveryPartner();
		    partner.setName(name);
		    partner.setEmail(email);
		    partner.setPassword(password);
		    partner.setServingPincode(servingPincode);
		    partner.setPhoneNo(phoneNo);
		    partner.setCurrLocLongitude(currLocLongitude);
		    partner.setCurrLocLatitude(currLocLatitude);
		    partner.setIdentityProof(uploadedFileName);
		    partner.setIsAvail(true); // Default value

		    // Save to database
		    try {
		        DeliveryPartner registeredPartner = deliveryPartnerService.registerDeliveryPartner(partner);
		        if (registeredPartner != null) {
		            return ResponseEntity.status(HttpStatus.CREATED).body(registeredPartner);
		        }
		        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already registered.");
		    } catch (IllegalArgumentException e) {
		        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
		    }
		}

		@PostMapping("login-delivery-partner")
		public ResponseEntity<Map<String, Object>> authenticateDeliveryPartner(@RequestBody Map<String, Object> mp) {
		    String email = (String) mp.get("email");
		    String password = (String) mp.get("password");
		    DeliveryPartner deliveryPartner = deliveryPartnerService.authenticateDeliveryPartner(email, password);

		    if (deliveryPartner != null) {
		      
		        Map<String, Object> response = new HashMap<>();
		        response.put("deliveryPartnerId", deliveryPartner.getDeliveryPartnerId());
		        response.put("name", deliveryPartner.getName());
		        response.put("email", deliveryPartner.getEmail());
		        response.put("isLoggedIn", true);

		        return ResponseEntity.ok(response);
		    } else {
		        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid email or password."));
		    }
		}

		
		@GetMapping("get-pending-order")
		public List<PendingOrderStatus> getPendingOrderStatusHandler()
		{
			return deliveryPartnerService.getPendingOrders();
		}
		
		
	
}
