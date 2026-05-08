package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.domain.Part;

public interface PartRepository extends CrudRepository<Part, Long> {
	@Query("SELECT p FROM Part p WHERE p.name LIKE %?1%")
	public List<Part> search(String keyword);
}
