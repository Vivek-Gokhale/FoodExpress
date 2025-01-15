package com.foodexpress.customer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodexpress.customer.model.CustomerAddress;

@Repository
public interface CustomerAddressDao extends JpaRepository<CustomerAddress, Integer> {
    List<CustomerAddress> findAllByUserId(int userId);
}
