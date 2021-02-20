package br.com.sisdb.dscatalog.resources;

import java.net.URI;

import br.com.sisdb.dscatalog.dto.UriDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.sisdb.dscatalog.dto.ProductDTO;
import br.com.sisdb.dscatalog.services.ProductService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {
   
	@Autowired
	private ProductService service;
	
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAll(
			@RequestParam(value = "categoryId", defaultValue = "0") Long categoryId,
			@RequestParam(value = "name", defaultValue = "") String name,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
			@RequestParam(value = "direction", defaultValue = "DESC") String direction) {
				
		PageRequest pageRequest	= PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);	
		Page<ProductDTO> list = service.findAllPaged(categoryId, name.trim(),  pageRequest);
		return ResponseEntity.ok(list); 
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id)  {
		ProductDTO dto = service.findById(id);		
		return ResponseEntity.ok(dto); 
	}
	
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) {
		dto = service.inster(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				  .buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
	@PostMapping(value = "/image")
	public ResponseEntity<UriDTO> uploadImage(@RequestParam(value = "file") MultipartFile file) {

		String uriImg = service.salvarImageProduct(file);
        UriDTO uriDto = new UriDTO();
        uriDto.setUri(uriImg);
		return ResponseEntity.ok(uriDto);
	}
	
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
		
		dto = service.update(id, dto);
		return ResponseEntity.ok(dto);
	}
	
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> delete(@PathVariable Long id) {
		
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	
	
}


















