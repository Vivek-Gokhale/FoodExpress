package com.foodexpress.customer.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.foodexpress.customer.model.Customer;

public interface CustomerDao extends JpaRepository<Customer, Integer>{
	Optional<Customer> findByEmail(String email);
	Optional<Customer> findByEmailAndPassword(String email, String password);
}
