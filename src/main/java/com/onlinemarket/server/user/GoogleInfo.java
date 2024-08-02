package com.onlinemarket.server.user;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "google.client")
@Data
public class GoogleInfo {
    private String id;
    private String secret;

}
