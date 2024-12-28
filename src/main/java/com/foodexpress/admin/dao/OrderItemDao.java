package com.foodexpress.admin.dao;

import com.foodexpress.admin.dto.CustomerMenuPreferenceStats;
import com.foodexpress.admin.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemDao extends JpaRepository<OrderItem, Integer> {

    public List<OrderItem> findByRestaurantId(Integer restaurantId);
    public List<OrderItem> findByOrderId(Integer orderId);

    @Query(value = "SELECT m.name AS dishName, " +
            "COUNT(oi.item_id) AS orderCount, " +
            "GROUP_CONCAT(DISTINCT oi.special_request ORDER BY oi.special_request) AS customizations " +
            "FROM order_items oi " +
            "JOIN menu_items m ON oi.item_id = m.item_id " +
            "WHERE oi.restaurant_id = :restaurantId " +
            "GROUP BY oi.item_id, m.name " +
            "ORDER BY orderCount DESC", nativeQuery = true)
    List<Object[]> findCustomerMenuPreferenceStatsByRestaurantId(@Param("restaurantId") int restaurantId);

}
