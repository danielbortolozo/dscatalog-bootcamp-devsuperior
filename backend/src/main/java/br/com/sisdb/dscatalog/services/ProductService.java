package br.com.sisdb.dscatalog.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sisdb.dscatalog.dto.CategoryDTO;
import br.com.sisdb.dscatalog.dto.ProductDTO;
import br.com.sisdb.dscatalog.entities.Category;
import br.com.sisdb.dscatalog.entities.Product;
import br.com.sisdb.dscatalog.repositories.CategoryRepository;
import br.com.sisdb.dscatalog.repositories.ProductRepository;
import br.com.sisdb.dscatalog.services.exceptions.DataBaseException;
import br.com.sisdb.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(Long categoryId, String name,  PageRequest pageRequest) {
		List<Category> categories = (categoryId == 0) ? null : Arrays.asList(categoryRepository.getOne(categoryId));
		Page<Product> list = repository.find(categories, name,  pageRequest);
		return list.map(x -> new ProductDTO(x));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById (Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));		
		return new ProductDTO(entity, entity.getCategories());
	}

	@Transactional
	public ProductDTO inster(ProductDTO dto) {		
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
		  Product entity = repository.getOne(id);
		
	      copyDtoToEntity(dto, entity);
		  entity = repository.save(entity);
		  return new ProductDTO(entity);
		}
		 catch (EntityNotFoundException e) {
			 throw new ResourceNotFoundException("Id not found "+ id);
		 }
	}

	public void delete(Long id) {
		try {
		  repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found "+ id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation");
		}
	}

    private void copyDtoToEntity(ProductDTO dto, Product entity) {	
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		entity.setDate(dto.getDate());
		
		entity.getCategories().clear();
		for (CategoryDTO catDto : dto.getCategories()) {
			Category category = categoryRepository.getOne(catDto.getId());
			entity.getCategories().add(category);
		}
		
		
	}
}
