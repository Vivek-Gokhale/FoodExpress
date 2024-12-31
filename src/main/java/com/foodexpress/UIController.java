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
	 
	
}
