package com.onlinemarket.server.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.onlinemarket.server.order.Order;
import com.onlinemarket.server.shoppingcart.ShoppingCart;
import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
// @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer user_id;

    @Column(unique = true)
    @Email
    private String email;

    private String countryCode;

    @Column(unique = true)
    private Integer phoneNumber;

    private String username;

    @Column(nullable = false)
    private String salt;

    @Column(nullable = false)
    @NotBlank
    private String password;

    @Column(nullable = false)
    @NotBlank
    private String firstName;

    @Column(nullable = false)
    @NotBlank
    private String lastName;

    // @Column(nullable = false)
    // @NotBlank
    // private String address;

    // primary entity
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ShoppingCart shoppingCart;

    @OneToMany(mappedBy = "user")
    // prevent child to serialize parent
    @JsonManagedReference
    private List<Order> orders;

    @Column(nullable = false)
    private Timestamp createdTime;

    @Column(nullable = false)
    private Timestamp updatedTime;

    @AssertTrue(message = "country code and phone number or email is required")
    private boolean isPhoneNumberOrEmailChecker() {
        return (countryCode != null && phoneNumber != null) || email != null;
    }

    @AssertTrue(message = "phone number format is not appropriate")
    private boolean isPhoneNumberFormatChecker() {
        if (phoneNumber == null || countryCode == null) {
            return true;
        }

        int phoneNumberLength = String.valueOf(phoneNumber).length();
        System.out.println("test");
        if (countryCode.equals("+852")) {

            return phoneNumberLength == 8;
        } else if (countryCode.equals("+886")) {

            return phoneNumberLength == 8 || phoneNumberLength == 9;
        }
        return false;
    }

}
