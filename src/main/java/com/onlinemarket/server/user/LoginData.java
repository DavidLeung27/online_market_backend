package com.onlinemarket.server.user;

import jakarta.validation.constraints.*;

public record LoginData(
                @NotBlank String loginMethod,

                String username,

                @Email String email,

                String countryCode,

                Integer phoneNumber,

                @NotBlank String password) {
        @AssertTrue(message = "country code and phone number or email or username is required")
        private boolean isPhoneNumberOrEmailOrUsernameExists() {
                return (countryCode != null && phoneNumber != null) || email != null || username != null;
        }

        @AssertTrue(message = "login method does not exists")
        private boolean isLoginMethodExists() {
                switch (loginMethod) {
                        case "PhoneLogin":
                                ;
                        case "EmailLogin":
                                ;
                        case "UsernameLogin":
                                ;
                                return true;
                }
                return false;
        }
}
