package com.onlinemarket.server.user;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.onlinemarket.server.constant.Constant;
import com.onlinemarket.server.jwt.JwtHandler;
import com.onlinemarket.server.result.Result;

import reactor.core.publisher.Mono;

// import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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

    @Autowired
    private Constant constant;

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

    public Result LoginByOauth2Google(String authorizationHeader) throws GeneralSecurityException, IOException {

        System.out.println("LoginByOauth2Google Request");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
            System.out.println("No authorization code from request");
            return new Result(0, "Authorization code are null or format is not correct",
                    null);
        }

        String authCode = authorizationHeader.replace("Bearer ", "");

        String CLIENT_ID = constant.getId();
        System.out.println("CLIENT_ID: " + CLIENT_ID);
        String CLIENT_SECRET = constant.getSecret();
        System.out.println("CLIENT_SECRET: " + CLIENT_SECRET);
        String REDIRECT_URI = constant.getRedirect();
        System.out.println("REDIRECT_URI: " + REDIRECT_URI);
        System.out.println("authCode: " + authCode);

        Map<String, Object> body = new HashMap<>();
        body.put("client_id", CLIENT_ID);
        body.put("client_secret", CLIENT_SECRET);
        body.put("code", authCode);
        body.put("grant_type", "authorization_code");
        body.put("redirect_uri", REDIRECT_URI);

        WebClient.Builder webclientBuilder = WebClient.builder();

        System.out.println("Send Request to googleapis to exchange auth code to token");

        Mono<String> googleResponse = webclientBuilder.build()
                .post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class);

        System.out.println("Googleapis responsed");
        System.out.println(googleResponse);

        String id_token = null;

        try {
            id_token = googleResponse.map(responseBody -> {
                JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
                return json.get("id_token").getAsString();
            }).block();

        } catch (Exception e) {
            System.out.println("Verification failure");
            return new Result(0, "Verification failure", null);
        }

        if (id_token == null) {
            System.out.println("Verification failure");
            return new Result(0, "Verification failure", null);
        }

        System.out.println("Verification success");

        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport,
                jsonFactory)
                .setAudience(Collections.singletonList(CLIENT_ID))
                .build();

        GoogleIdToken idToken = verifier.verify(id_token);

        if (idToken == null) {
            System.out.println("Google JWT Decdoe Fail");
            return new Result(0, "Google JWT Decdoe Fail", null);
        }

        System.out.println("Google JWT Decdoe Success");
        GoogleIdToken.Payload payload = idToken.getPayload();

        String email = payload.getEmail();

        String jwt = jwtHandler.generateToken(email);

        System.out.println("Self JWT generated: " + jwt);

        return new Result(1, "Verification success", jwt);
    }

}
