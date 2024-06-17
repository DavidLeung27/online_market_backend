package com.onlinemarket.server.productCategory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByCategory(String category);
}
