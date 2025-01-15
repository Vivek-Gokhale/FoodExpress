package com.foodexpress.customer.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodexpress.customer.model.AccountPreference;
import com.foodexpress.customer.model.Customer;



@Repository
public interface AccountPreferenceDao extends JpaRepository<AccountPreference, Integer>{
	Optional<AccountPreference> findByUserId(int userId);
}
