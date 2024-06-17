package com.onlinemarket.server.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);

    User findByCountryCodeAndPhoneNumber(String countryCode, Integer phoneNumber);

    User findByEmailAndPassword(String username, String password);

    User findByCountryCodeAndPhoneNumberAndPassword(String countryCode, Integer phoneNumber, String password);
}
