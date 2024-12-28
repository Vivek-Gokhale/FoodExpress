package com.foodexpress.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodexpress.customer.model.AccountPreference;
import com.foodexpress.customer.service.AccountPreferenceService;

@RestController
public class AccountPreferenceController {

	
    @Autowired
    private AccountPreferenceService accountPreferenceService;

    @PostMapping("get-account-preference/{userId}")
    public AccountPreference handleAccountPreference(@PathVariable("userId") int userId) {
        // Fetch account preference for the given userId
        return accountPreferenceService.getAccountPreference(userId);
    }

    @PostMapping("update-account-preference")
    public boolean handleUpdateAccountPreference(@RequestBody String accountPreferenceJson) {
        try {
            // Convert JSON string to AccountPreference object
            ObjectMapper objectMapper = new ObjectMapper();
            AccountPreference accountPreference = objectMapper.readValue(accountPreferenceJson, AccountPreference.class);

            // Update the account preference
            return accountPreferenceService.updatePreference(accountPreference);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false; // Handle the exception (you might want to return a better response in real use)
        }
    }

}
