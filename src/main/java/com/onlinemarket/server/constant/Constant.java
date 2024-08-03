package com.onlinemarket.server.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// import com.google.api.client.util.Value;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "google.client")
@Data
public class Constant {
    // @Value("${google.client.id}")
    private String id;

    // @Value("${google.client.secret}")
    private String secret;

    // @Value("${google.client.redirect}")
    private String redirect;

}
