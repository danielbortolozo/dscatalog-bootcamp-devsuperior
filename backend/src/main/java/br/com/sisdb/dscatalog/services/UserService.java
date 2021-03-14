package br.com.sisdb.dscatalog.services;

import br.com.sisdb.dscatalog.dto.RoleDTO;
import br.com.sisdb.dscatalog.dto.UserDTO;
import br.com.sisdb.dscatalog.dto.UserInsertDTO;
import br.com.sisdb.dscatalog.dto.UserUpdateDTO;
import br.com.sisdb.dscatalog.entities.Role;
import br.com.sisdb.dscatalog.entities.User;
import br.com.sisdb.dscatalog.repositories.RoleRepository;
import br.com.sisdb.dscatalog.repositories.UserRepository;
import br.com.sisdb.dscatalog.services.exceptions.DataBaseException;
import br.com.sisdb.dscatalog.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository repository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private RoleRepository roleRepository;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(PageRequest pageRequest) {
		Page<User> list = repository.findAll(pageRequest);
		return list.map(x -> new UserDTO(x));
	}

	@Transactional(readOnly = true)
	public UserDTO findById (Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));		
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO inster(UserInsertDTO dto) {
		User entity = new User();
		copyDtoToEntity(dto, entity);
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = repository.save(entity);
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
		  User entity = repository.getOne(id);
		
	      copyDtoToEntity(dto, entity);
		  entity = repository.save(entity);
		  return new UserDTO(entity);
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
			throw new ResourceNotFoundException("Id not found :"+ id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity violation, Error: "+e.getMessage());
		}
	}

    private void copyDtoToEntity(UserDTO dto, User entity) {	

		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());

		entity.getRoles().clear();
		for(RoleDTO roleDto : dto.getRoles()) {
            Role role = roleRepository.getOne(roleDto.getId());
			entity.getRoles().add(role);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository.findByEmail(username);
		if (user == null) {
			logger.error("user not found: "+ username);
			throw new UsernameNotFoundException("Email not found.");
		}
		logger.info("User found: "+username);
		return user;
	}
}
