package br.com.sisdb.dscatalog.entities;

import br.com.sisdb.dscatalog.dto.ProductDTO;

import java.time.Instant;
import java.util.Date;

public class ProductFactory {


    public static Product createProduct() {
       Date dtCad = new Date();
       Product product = new Product(1L, "IPhone good", "teste de unidade",  100.0, "/img", dtCad.toInstant());
       product.getCategories().add(new Category(1L, null));
       return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }

    public static ProductDTO createProductDTO(Long id) {
        ProductDTO dto = createProductDTO();
        dto.setId(id);
        return dto;
    }
}
