package com.onlinemarket.server.order;

import com.onlinemarket.server.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
