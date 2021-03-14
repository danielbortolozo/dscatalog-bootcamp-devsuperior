package br.com.sisdb.dscatalog.repositories;

import br.com.sisdb.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTest {

   @Autowired
   private ProductRepository repository;

   private long existingId;
   private long nonExistingId;
   @BeforeEach
   void setUp() throws Exception {
       existingId = 1L;
       nonExistingId = 10000L;
   }

   @Test
   public void deleteShouldDeletedObjectWhenIdExists() {
       repository.deleteById(existingId);
       Optional<Product> result = repository.findById(existingId);
       Assertions.assertFalse(result.isPresent());
   }

    @Test
    public void deleteShouldThrowEEmptyResultDataAccessExceptionWhenIdDoesNotExists() {

       Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
           repository.deleteById(nonExistingId);
       });
    }


}




