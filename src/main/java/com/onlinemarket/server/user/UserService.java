package com.onlinemarket.server.user;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.onlinemarket.server.jwt.JwtHandler;
import com.onlinemarket.server.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

@Service
@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHandler jwtHandler;

    public Result CreateUser(User user) {
        String email = user.getEmail();
        User userAlreadyExists;
        if (email != null) {
            userAlreadyExists = userRepository.findByEmail(email);
        } else {
            userAlreadyExists = userRepository.findByCountryCodeAndPhoneNumber(user.getCountryCode(),
                    user.getPhoneNumber());
        }
        if (userAlreadyExists != null) {
            return new Result(0, "Account already exists.", userAlreadyExists);
        }

        final Random random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        user.setSalt(Arrays.toString(salt));
        System.out.println("Salt:" + Arrays.toString(salt));

        String encodedPassword = passwordEncoder.encode(user.getPassword() + Arrays.toString(salt));

        user.setPassword(encodedPassword);

        Timestamp currentTime = Timestamp.from(Instant.now());
        user.setCreatedTime(currentTime);
        user.setUpdatedTime(currentTime);
        return new Result(1, "Account created", userRepository.save(user));
    }

    public Result Login(
            LoginData loginData) {
        User user;

        if (loginData.loginMethod().equals("PhoneLogin")) {
            user = userRepository.findByCountryCodeAndPhoneNumber(loginData.countryCode(), loginData.phoneNumber());
        } else if (loginData.loginMethod().equals("EmailLogin")) {
            user = userRepository.findByEmail(loginData.email());
        } else {
            user = userRepository.findByEmail(loginData.email());
        }

        if (user == null
                || Objects.equals(user.getPassword(), passwordEncoder.encode(user.getPassword() + user.getSalt()))) {
            return new Result(0, "Account does not exists", null);
        }

        String jwt = jwtHandler.generateToken(user.getUser_id());

        return new Result(1, "Login Success", jwt);
    }

    public Result JWTChecker(String jwtHeader) {
        if (jwtHeader == null) {
            System.out.println("No jwt header from request");
            return new Result(0, "Jwt should be provided in header", null);
        }

        String jwtCheckMessage = jwtHandler.checkToken(jwtHeader);

        if (!jwtCheckMessage.startsWith("jwt updated:")) {
            return new Result(0, jwtCheckMessage, null);
        }

        jwtCheckMessage = jwtCheckMessage.substring(12);

        return new Result(1, "Jwt is valid", jwtCheckMessage);
    }

    public Result LoginByOauth2Google(
            String authorizationHeader) throws GeneralSecurityException, IOException {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("No authorization code from request");
            return new Result(0, "Authorization code are null or format is not correct", null);
        }

        String token = authorizationHeader.replace("Bearer ", "");
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        final String CLIENT_ID = "510468186222-ut83218bhe7j3e908oh417vj3algu10l.apps.googleusercontent.com";

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken;

        idToken = verifier.verify(token);

        if (idToken == null) {
            System.out.println("Verification failure");
            return new Result(0, "Verification fail", null);
        }

        System.out.println("Verification success");
        GoogleIdToken.Payload payload = idToken.getPayload();
        String userId = payload.getSubject();
        String email = payload.getEmail();

        return new Result(1, "Verification success", null);
    }

}
