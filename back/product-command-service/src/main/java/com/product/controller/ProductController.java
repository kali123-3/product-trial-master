package com.product.controller;

import com.product.entity.Product;
import com.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/dapi/products")
public class ProductController {
    public final static String contentType = "application/octet-stream";

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping
    public Product createProduct(@Valid @RequestBody Product product) {
        return productService.createProduct(product);
    }

    @PatchMapping("/{id}")
    public Product updateProduct(@PathVariable long id, @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable long id) {
        return new ResponseEntity<>(productService.deleteProduct(id), HttpStatus.OK);
    }

    @PostMapping("/uploadImageProduct/{id}")
    public ResponseEntity<String> uploadImageProduct(@RequestParam("file") MultipartFile file, @PathVariable long id) {
        return new ResponseEntity<>(productService.uploadImageProduct(file, id),HttpStatus.OK);
    }

    @GetMapping("/downloadImageProduct/{imageName}")
    public ResponseEntity<Resource> downloadImageProduct(@PathVariable String imageName) throws IOException {
        Resource resource = productService.downloadImageProduct(imageName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageName + "\"")
                .body(resource);
    }
}