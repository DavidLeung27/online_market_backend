package com.onlinemarket.server.product;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.onlinemarket.server.result.Result;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product")
    public Result GetProduct(@RequestParam String productName, @RequestParam String category,
            @RequestParam(required = true) Integer page) {
        System.out.println(category);
        System.out.println(page);
        System.out.println(productName);
        return productService.GetProduct(category, page);
    }

    @GetMapping("/product/v2")
    public Result GetProductV2(ProductFilterCiteria productFilterCiteria) {

        return productService.GetProductV2(productFilterCiteria);
    }

    @PostMapping("/addProduct")
    public Result AddProduct(@RequestPart("image") MultipartFile image, @RequestPart("product") String productJson) {

        return productService.AddProduct(image, productJson);
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