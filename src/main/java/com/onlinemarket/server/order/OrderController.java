package com.onlinemarket.server.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping("/orders")
    public Order CreateOrder(
            @RequestBody Order order
    )
    {
        orderRepository.save(order);
        return order;
    }

    @GetMapping("/orders")
    public List<Order> GetOrders() {
        return orderRepository.findAll();
    }

//    @GetMapping("/orders")
//    public List<Order> GetOrders(
//            @RequestBody Integer userId
//    ) {
//        return orderRepository.findAll();
//    }
}
