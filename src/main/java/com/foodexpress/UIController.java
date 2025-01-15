package com.foodexpress;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UIController {
	
	@GetMapping("/")
    public String redirectToIndex() {
        return "index";
    }
	
	@GetMapping("customer-login")
	public String promptLogin()
	{
		return "login";
	}
	
	@GetMapping("admin-login")
	public String promptAdminLogin()
	{
		return "adminLogin";
	}
	@GetMapping("partner-login")
	public String promptPartnerLogin()
	{
		return "partnerLogin";
	}
	
	@GetMapping("user-address")
	public String promptUserAddress()
	{
		return "userAddress";
	}
	
	@GetMapping("settings")
	public String promptSettings()
	{
		return "settings";
	}
	
	@GetMapping("order-history")
	public String promptOrderHistory()
	{
		return "orderHistory";
	}
	
	@GetMapping("reset-password")
	public String promptResetPassword()
	{
		return "resetPassword";
	}
	
	@GetMapping("signup")
	public String promptSignup()
	{
		return "signup";
	}
	
	@GetMapping("forgot-password")
	public String promptforgotPassword()
	{
		return "forgotPassword";
	}
	@GetMapping("index")
	public String promptIndexPage()
	{
		return "index";
	}
	 @GetMapping("foodSlider")
	    public String foodSlider() {
	        return "foodSlider"; // Thymeleaf automatically looks for foodSlider.html in the templates folder
	    }
	 @GetMapping("add-restaurant")
	    public String showAddRestaurantForm(Model model) {
	        model.addAttribute("currentDate", LocalDate.now());
	        return "addRestaurant";
	    }
	 
	 @GetMapping("/add-admin")
	 public String showAddAdminPage(@RequestParam("restaurantId") int restaurantId, Model model) {
	     model.addAttribute("restaurantId", restaurantId);
	     return "addAdmin";
	 }
	 
	 @GetMapping("/be-partner")
	 public String showBePartner() {
	     
	     return "bePartner";
	 }

	 @GetMapping("/dashboard")
	 public String showDashboard() {
	     
	     return "dashboard";
	 }
	 @GetMapping("/add-restaurant-address")
	 public String showAddAddressPage(@RequestParam("restaurantId") int restaurantId, Model model) {
	     model.addAttribute("restaurantId", restaurantId);
	     return "addAddress";
	 }
	 @GetMapping("/delivery-partner-dashboard")
	 public String showPartnerDashboard(@RequestParam("name") String name, Model model) {
	     model.addAttribute("name", name);
	     return "deliveryPartner";
	 }
	 
	 @GetMapping("/food")
	 public String showFoodMenu()
	 {
		 return "food";
	 }
	 
	 @GetMapping("/single-food-page")
	 public String showSingleFoodPage() {
	     
	     return "singleFoodPage";
	 }
	 
	 @GetMapping("/add-customer-address")
	 public String showAddAddressPageCustomer() {
	     return "addAddress";
	 }
	 
	 @GetMapping("/edit-customer-address")
	 public String showEditAddressPageCustomer() {
	     return "editAddress";
	 }
	 
	 @GetMapping("/create-review")
	 public String showCreateReview(
	         @RequestParam("uid") int userId, 
	         @RequestParam("rid") int restaurantId, 
	         @RequestParam("itemname") String itemName, 
	         @RequestParam("image") String image, 
	         @RequestParam("rname") String restaurantName,
	         @RequestParam("mid") int mid,
	         @RequestParam("oid") int oid,
	         Model model) {
	     
	     // Add the captured values to the model
	     model.addAttribute("userId", userId);
	     model.addAttribute("restaurantId", restaurantId);
	     model.addAttribute("itemName", itemName);
	     model.addAttribute("image", image);
	     model.addAttribute("restaurantName", restaurantName);
	     model.addAttribute("itemId", mid);
	     
	     // Return the name of the view
	     return "createReview";
	 }
	 
	 @GetMapping("/add-to-cart")
	 public String showAddToCart()
	 {
		 return "addToCart";
	 }

	 

	 
	
}
