package com.product;

import com.product.entity.Product;
import com.product.repository.ProductRepository;
import com.product.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

        @Mock
        private ProductRepository productRepository;

        @InjectMocks
        private ProductService productService;

        @Test
        void getAllProductsTest() {
            List<Product> productsMocks = Arrays.asList(
                    new Product(1L,"code1"),
                    new Product(2L,"code2"));

            Mockito.when(productRepository.findAll()).thenReturn(productsMocks);
            List<Product> result = productService.getAllProducts();

            Assertions.assertFalse(result.isEmpty());
            Assertions.assertEquals(productsMocks, result);
        }

        @Test
        void createProductTest() {
            Product mockProduct = Product.builder().code("code1").build();
            Product savedProduct = new Product(1L,"code1");

            Mockito.when(productRepository.save(mockProduct)).thenReturn(savedProduct);

            Product result = productService.createProduct(mockProduct);

            Assertions.assertEquals("code1", result.getCode());
            Assertions.assertEquals(1L, result.getId());

        }


    @Test
    void DeleteProductTest() {
        Optional<Product> product = Optional.of(new Product(1L,"code1"));
        Mockito.when(productRepository.findById(1L)).thenReturn(product);

        productService.deleteProduct(1L);

        Mockito.verify(productRepository, times(1)).findById(1L);
        Mockito.verify(productRepository, times(1)).delete(product.get());
    }



}
