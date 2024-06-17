package com.onlinemarket.server.product;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record ProductResult(
                @NotNull Integer noOfPage,

                List<Product> searchProduct) {

}
