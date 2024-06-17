package com.onlinemarket.server.productCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlinemarket.server.result.Result;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Result GetAllCategory() {
        // List<Category> allCategory = categoryRepository.findAll();
        return new Result(1, "Get all category success", categoryRepository.findAll());
    }

    public Result AddCategory(Category category) {

        if (categoryRepository.findByCategory(category.getCategory()) != null) {
            return new Result(0, "Category already exists", null);
        }

        categoryRepository.save(category);
        return new Result(1, "Add Category Success", null);
    }
}
