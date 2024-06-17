package com.onlinemarket.server.productCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.onlinemarket.server.result.Result;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @GetMapping("/category")
    public Result GetAllCategory() {
        return categoryService.GetAllCategory();
    }

    @PostMapping("/addCategory")
    public Result AddCategory(@RequestBody Category category) {
        return categoryService.AddCategory(category);
    }

}
