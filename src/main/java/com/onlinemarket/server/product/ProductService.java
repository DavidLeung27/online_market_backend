package com.onlinemarket.server.product;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlinemarket.server.result.Result;

@Service
@Component
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Result GetProduct(String category, Integer page) {
        Integer noOfResult = productRepository.countByLinkedCategory_Category(category);

        Pageable pageElements = PageRequest.of(page - 1, 20);
        List<Product> searchProducts = productRepository.findByLinkedCategory_Category(category, pageElements);

        // ProductResult productResult = new ProductResult(noOfResult, searchProducts);

        // return new Result(1, "test", productResult);
        return new Result(1, "test", null);
    }

    public Result GetProductV2(ProductFilterCiteria productFilterCiteria) {
        Pageable pageElement = PageRequest.of(productFilterCiteria.getPage() - 1,
                productFilterCiteria.getPageSize(), Sort.by("hitRate").descending());

        Page<Product> filteredProducts = productRepository
                .findAll(PrdouctSpecifications.filterByCriteria(productFilterCiteria),
                        pageElement);

        ProductResult productResult = new ProductResult(filteredProducts.getTotalPages(),
                filteredProducts.getContent());

        return new Result(1, "It is still developing", productResult);

    }

    public Result AddProduct(MultipartFile image, String productJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Product product = objectMapper.readValue(productJson, Product.class);
            product.setImage(image.getBytes());

            Timestamp currentTime = Timestamp.from(Instant.now());
            product.setCreatedTime(currentTime);
            product.setUpdatedTime(currentTime);
            productRepository.save(product);

            return new Result(1, "New product launched", null);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(0, "Product cannot be launched", e);
        }
    }

}