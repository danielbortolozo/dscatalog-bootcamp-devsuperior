package br.com.sisdb.dscatalog.entities;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import br.com.sisdb.dscatalog.dto.CategoryDTO;


@Entity
@Table(name = "category")
public class Category implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;	
	
	private String name;
	
	@Column(columnDefinition="TIMESTAMP WITHOUT TIME ZONE")
	private Instant createAt;

	@Column(columnDefinition="TIMESTAMP WITHOUT TIME ZONE")
	private Instant updateAt;

	
	public Category(Long id, String name) {		
		this.id = id;
		this.name = name;
	}

	public Category() {
		
	}

	public Category(CategoryDTO dto) {
		
		this.name = dto.getName();
	}

	//Gets e Sets
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Instant getCreateAt() {
		return createAt;
	}

	public Instant getUpdateAt() {
		return updateAt;
	}

	/**
	 * Auditoria de create new category.
	 * */
	@PrePersist
	public void prePersist() {
		createAt = Instant.now();
	}
	
	@PreUpdate
	public void preUpdate() {
		updateAt = Instant.now();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	

}
