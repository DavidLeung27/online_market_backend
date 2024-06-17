package com.onlinemarket.server.product;

import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFilterCiteria {
    private String productName;
    private String category;
    private Integer page;
    private Integer pageSize;
    private Float minPrice;
    private Float maxPrice;
    private Boolean promation;
}
