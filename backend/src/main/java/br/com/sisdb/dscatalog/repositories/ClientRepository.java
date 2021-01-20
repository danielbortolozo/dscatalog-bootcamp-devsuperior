package br.com.sisdb.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sisdb.dscatalog.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>{

}
