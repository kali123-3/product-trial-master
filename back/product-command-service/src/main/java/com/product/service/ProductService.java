package com.product.service;

import com.product.entity.Product;
import com.product.exceptions.ManageFileException;
import com.product.exceptions.ProductNotFoundException;
import com.product.exceptions.ResourceNotFoundException;
import com.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    private static final String UPLOAD_DIR = "C://Users//user//tuto";
    private static final String SEPARATOR = "___";

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Transactional
    public Product createProduct(Product product){
        product.setCreatedAt(LocalDateTime.now());
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(long id,Product product){
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isEmpty()) {
            throw new RuntimeException("Product not found with id: " + id);
        }

        Product updatedProduct = Product.builder()
                .id(existingProduct.get().getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .image(product.getImage())
                .category(product.getCategory())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .internalReference(product.getInternalReference())
                .shellId(product.getShellId())
                .inventoryStatus(product.getInventoryStatus())
                .rating(product.getRating())
                .createdAt(existingProduct.get().getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        return productRepository.save(updatedProduct);
    }

    @Transactional
    public Boolean deleteProduct(Long idProduct){
        Optional<Product> product = productRepository.findById(idProduct);
        if(product.isPresent()){
            productRepository.delete(product.get());
            return true;
        }else{
            throw new ProductNotFoundException("Product not found with id: " + idProduct);
        }
    }

    public String uploadImageProduct(MultipartFile file, long idProduct) {
        if (file.isEmpty()) {
            throw new ManageFileException("No file uploaded");
        }
        try {
            String filename = StringUtils.cleanPath(file.getOriginalFilename());
            Path targetLocation = Paths.get(UPLOAD_DIR, idProduct + SEPARATOR + filename);
            updateProductImagePath(idProduct, filename);
            Files.copy(file.getInputStream(), targetLocation);

            return "File uploaded successfully";
        } catch (IOException ex) {
            throw new ManageFileException("Error uploading file: " + ex.getMessage());
        }
    }

    public void updateProductImagePath(long idProduct, String filename) {
        Optional<Product> existingProduct = productRepository.findById(idProduct);
        if (existingProduct.isEmpty()) {
            throw new RuntimeException("Product not found with id: " + idProduct);
        }
        existingProduct.get().setImage(idProduct + SEPARATOR + filename);
        updateProduct(idProduct, existingProduct.get());
    }

    public Resource downloadImageProduct(@PathVariable String imageName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(imageName).normalize();

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new ResourceNotFoundException("Resource not found with name: " + imageName);
        }
        return resource;
    }


}
