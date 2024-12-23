package com.foodexpress.admin.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.foodexpress.admin.model.RestaurantRegister;

@Repository
public interface RestaurantRegisterDao extends JpaRepository<RestaurantRegister, Integer> {
    Optional<RestaurantRegister> findRestaurantIdByEmail(String email);
}
