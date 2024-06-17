package com.onlinemarket.server.user;

import com.onlinemarket.server.result.Result;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public Result CreateUser(
            @Valid @RequestBody User user) {
        return userService.CreateUser(user);
    }

    @PostMapping("/login")
    public Result Login(
            @Valid @RequestBody LoginData loginData) {
        return userService.Login(loginData);
    }

    @PostMapping("/login/jwtChecker")
    public Result JwtChecker(@RequestHeader("Jwt") String jwtHeader) {
        return userService.JWTChecker(jwtHeader);
    }

    @PostMapping("/login/oauth/google")
    public Result LoginByOauth2Google(@RequestHeader("Authorization") String authorizationHeader)
            throws GeneralSecurityException, IOException {
        return userService.LoginByOauth2Google(authorizationHeader);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exp) {
        var errors = new HashMap<String, String>();

        exp.getBindingResult().getAllErrors().forEach(error -> {
            var fieldName = ((FieldError) error).getField();
            var errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
