package br.com.sisdb.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sisdb.dscatalog.dto.CategoryDTO;
import br.com.sisdb.dscatalog.entities.Category;
import br.com.sisdb.dscatalog.repositories.CategoryRepository;
import br.com.sisdb.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll() {
		List<Category> list = categoryRepository.findAll();
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById (Long id) {
		Optional<Category> obj = categoryRepository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));		
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO inster(CategoryDTO dto) {		
		Category entity = new Category(dto);
		entity = categoryRepository.save(entity);
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
		  Category entity = categoryRepository.getOne(id);
		
		  entity.setName(dto.getName());
		  entity = categoryRepository.save(entity);
		  return new CategoryDTO(entity);
		}
		 catch (EntityNotFoundException e) {
			 throw new ResourceNotFoundException("Id not found "+ id);
		 }
	}

}
