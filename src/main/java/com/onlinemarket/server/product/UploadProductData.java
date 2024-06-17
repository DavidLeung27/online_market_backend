package com.onlinemarket.server.product;

import org.springframework.web.multipart.MultipartFile;

public record UploadProductData(
        Product productData,

        MultipartFile image) {

}
