package com.onlinemarket.server.shoppingcart;

import com.onlinemarket.server.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "shoppping_carts")
public class ShoppingCart {
    @Id
    @GeneratedValue
    private Integer shoppingCart_id;

    @OneToOne
    @JoinColumn(referencedColumnName = "user_id", name = "user_id")
    private User user;
}
