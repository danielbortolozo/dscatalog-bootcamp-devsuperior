package br.com.sisdb.dscatalog.service;

import br.com.sisdb.dscatalog.entities.Product;
import br.com.sisdb.dscatalog.entities.ProductFactory;
import br.com.sisdb.dscatalog.repositories.ProductRepository;
import br.com.sisdb.dscatalog.services.ProductService;
import br.com.sisdb.dscatalog.services.exceptions.DataBaseException;
import br.com.sisdb.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;
    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Product product;
    private PageImpl<Product> page;

    @BeforeEach
    void setUp() throws Exception {
       existingId = 1L;
       nonExistingId = 1000L;
       dependentId = 4L;
       product = ProductFactory.createProduct();
       page = new PageImpl<>(List.of(product));

       Mockito.when(repository.find(ArgumentMatchers.any(), ArgumentMatchers.anyString(), ArgumentMatchers.any()))
               .thenReturn(page);
       Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

       Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
       Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

       Mockito.doNothing().when(repository).deleteById(existingId);
       Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
       Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenIdExistsRelation() {

        Assertions.assertThrows(DataBaseException.class,() -> {
            service.delete(dependentId);
        });
        Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {

        Assertions.assertThrows(ResourceNotFoundException.class,() -> {
            service.delete(nonExistingId);
        });
        Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }
}








