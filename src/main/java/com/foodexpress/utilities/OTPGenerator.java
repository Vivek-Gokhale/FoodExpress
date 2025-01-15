package com.foodexpress.utilities;

import java.util.Random;

public class OTPGenerator {
	 public static String generateOTP() {
	        Random random = new Random();
	        int otp = 100000 + random.nextInt(900000); // Ensures a 6-digit number
	        return String.valueOf(otp);
	    }
}
