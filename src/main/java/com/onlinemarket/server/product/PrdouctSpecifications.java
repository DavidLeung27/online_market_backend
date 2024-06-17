package com.onlinemarket.server.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

public class PrdouctSpecifications {

    public static Specification<Product> filterByCriteria(ProductFilterCiteria productFilterCiteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productFilterCiteria.getProductName() != null) {
                predicates.add(criteriaBuilder.equal(root.get("productName"), productFilterCiteria.getProductName()));
            }

            // if (productFilterCiteria.getCategory() != null) {
            // predicates.add(criteriaBuilder.equal(root.get("category"),
            // productFilterCiteria.getProductName()));
            // }

            if (productFilterCiteria.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("price"), productFilterCiteria.getMinPrice()));
            }
            System.out.println("here: " + predicates);

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}