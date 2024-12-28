package com.foodexpress.admin.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.foodexpress.admin.dto.CustomerOrderStats;
import com.foodexpress.admin.dto.OrderPerformance;
import com.foodexpress.admin.model.RestaurantOrder;

@Repository
public interface RestaurantOrderDao extends JpaRepository<RestaurantOrder, Integer> {

    @Query("""
        SELECT new com.foodexpress.admin.dto.CustomerOrderStats(
            o.userId,
            COUNT(o.orderId),  
            AVG(CASE WHEN o.orderDate >= :date7 AND o.orderDate IS NOT NULL THEN 1 ELSE 0 END), 
            AVG(CASE WHEN o.orderDate >= :date30 AND o.orderDate IS NOT NULL THEN 1 ELSE 0 END), 
            AVG(CASE WHEN o.orderDate >= :date365 AND o.orderDate IS NOT NULL THEN 1 ELSE 0 END),  
            SUM(CASE WHEN o.orderDate >= :date7 AND o.orderDate IS NOT NULL THEN 1 ELSE 0 END),  
            SUM(CASE WHEN o.orderDate >= :date30 AND o.orderDate IS NOT NULL THEN 1 ELSE 0 END), 
            SUM(CASE WHEN o.orderDate >= :date365 AND o.orderDate IS NOT NULL THEN 1 ELSE 0 END)  
        )
        FROM RestaurantOrder o
        JOIN OrderItem oi ON o.orderId = oi.orderId
        WHERE oi.restaurantId = :restaurantId
        GROUP BY o.userId
    """)
    List<CustomerOrderStats> findCustomerOrderStats(
        @Param("restaurantId") int restaurantId,
        @Param("date7") LocalDate date7,
        @Param("date30") LocalDate date30,
        @Param("date365") LocalDate date365
    );
 // Daily Report for a specific restaurant
    @Query(value = """
        SELECT 
            DATE(o.order_date) AS reportDate,
            COUNT(DISTINCT o.order_id) AS totalOrders,
            SUM(oi.price * oi.quantity) AS totalOrderValue,
            SUM(CASE WHEN o.delivery_status = 'completed' THEN 1 ELSE 0 END) AS completedOrders,
            SUM(CASE WHEN o.delivery_status = 'pending' THEN 1 ELSE 0 END) AS pendingOrders,
            SUM(CASE WHEN o.delivery_status = 'canceled' THEN 1 ELSE 0 END) AS canceledOrders
        FROM 
            orders o
        JOIN 
            order_items oi ON o.order_id = oi.order_id
        WHERE 
            oi.restaurant_id = :restaurantId
        GROUP BY 
            DATE(o.order_date)
        ORDER BY 
            reportDate DESC
        """, nativeQuery = true)
    List<Object[]> getDailyReportForRestaurant(@Param("restaurantId") Integer restaurantId);

    // Weekly Report for a specific restaurant
    @Query(value = """
        SELECT 
            YEAR(o.order_date) AS year,
            WEEK(o.order_date) AS weekNumber,
            COUNT(DISTINCT o.order_id) AS totalOrders,
            SUM(oi.price * oi.quantity) AS totalOrderValue,
            SUM(CASE WHEN o.delivery_status = 'completed' THEN 1 ELSE 0 END) AS completedOrders,
            SUM(CASE WHEN o.delivery_status = 'pending' THEN 1 ELSE 0 END) AS pendingOrders,
            SUM(CASE WHEN o.delivery_status = 'canceled' THEN 1 ELSE 0 END) AS canceledOrders
        FROM 
            orders o
        JOIN 
            order_items oi ON o.order_id = oi.order_id
        WHERE 
            oi.restaurant_id = :restaurantId
        GROUP BY 
            YEAR(o.order_date), WEEK(o.order_date)
        ORDER BY 
            year DESC, weekNumber DESC
        """, nativeQuery = true)
    List<Object[]> getWeeklyReportForRestaurant(@Param("restaurantId") Integer restaurantId);

    // Monthly Report for a specific restaurant
    @Query(value = """
        SELECT 
            YEAR(o.order_date) AS year,
            MONTH(o.order_date) AS month,
            COUNT(DISTINCT o.order_id) AS totalOrders,
            SUM(oi.price * oi.quantity) AS totalOrderValue,
            SUM(CASE WHEN o.delivery_status = 'completed' THEN 1 ELSE 0 END) AS completedOrders,
            SUM(CASE WHEN o.delivery_status = 'pending' THEN 1 ELSE 0 END) AS pendingOrders,
            SUM(CASE WHEN o.delivery_status = 'canceled' THEN 1 ELSE 0 END) AS canceledOrders
        FROM 
            orders o
        JOIN 
            order_items oi ON o.order_id = oi.order_id
        WHERE 
            oi.restaurant_id = :restaurantId
        GROUP BY 
            YEAR(o.order_date), MONTH(o.order_date)
        ORDER BY 
            year DESC, month DESC
        """, nativeQuery = true)
    List<Object[]> getMonthlyReportForRestaurant(@Param("restaurantId") Integer restaurantId);

}
