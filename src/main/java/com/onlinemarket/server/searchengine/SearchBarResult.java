package com.onlinemarket.server.searchengine;

import jakarta.validation.constraints.NotBlank;

public record SearchBarResult(
        @NotBlank String searchText) {

}
