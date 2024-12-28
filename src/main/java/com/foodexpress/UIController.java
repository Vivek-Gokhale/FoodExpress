package com.foodexpress;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UIController {
	
	@GetMapping("login")
	public String promptLogin()
	{
		return "login";
	}
	
	@GetMapping("resetPassword")
	public String promptResetPassword()
	{
		return "resetPassword";
	}
	
}
