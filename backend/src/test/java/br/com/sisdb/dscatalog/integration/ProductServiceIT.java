package br.com.sisdb.dscatalog.integration;

import br.com.sisdb.dscatalog.entities.Product;
import br.com.sisdb.dscatalog.entities.ProductFactory;
import br.com.sisdb.dscatalog.services.ProductService;
import br.com.sisdb.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@SpringBootTest
public class ProductServiceIT {

      @Autowired
      private ProductService service;

      private long existingId;
      private long nonExistingId;
      private Product product;
      private PageImpl<Product> page;

      @BeforeEach
      void setUp() throws Exception {
          existingId = 1L;
          nonExistingId = 1000L;
          product = ProductFactory.createProduct();
          page = new PageImpl<>(List.of(product));

      }

      @Test
      public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
          Assertions.assertThrows(ResourceNotFoundException.class,() -> {
              service.delete(nonExistingId);
          });
      }

      @Test
      public void deleteShouldDoNothingWhenIdExists() {
          Assertions.assertDoesNotThrow(() -> {
              service.delete(existingId);
          });
      }
  }

