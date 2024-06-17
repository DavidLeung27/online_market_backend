package com.onlinemarket.server.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.onlinemarket.server.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Integer order_id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "user_id", name = "user_id")
    @JsonBackReference
    private User user;
}
