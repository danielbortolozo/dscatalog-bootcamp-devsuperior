package br.com.sisdb.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.sisdb.dscatalog.dto.ClientDTO;
import br.com.sisdb.dscatalog.entities.Client;
import br.com.sisdb.dscatalog.repositories.ClientRepository;
import br.com.sisdb.dscatalog.services.exceptions.DataBaseException;
import br.com.sisdb.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository repository;
	
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(x -> new ClientDTO(x));
	}

	@Transactional(readOnly = true)
	public ClientDTO findById (Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));		
		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO inster(ClientDTO dto) {		
		Client entity = new Client(dto);
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
		  Client entity = repository.getOne(id);
		
		  entity.setName(dto.getName());
		  entity = repository.save(entity);
		  return new ClientDTO(entity);
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

}
