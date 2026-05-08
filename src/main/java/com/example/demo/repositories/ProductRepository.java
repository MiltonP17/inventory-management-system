package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.domain.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
	@Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
	public List<Product> search(String keyword);
}
