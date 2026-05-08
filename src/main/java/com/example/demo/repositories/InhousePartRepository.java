package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.domain.InhousePart;

public interface InhousePartRepository extends CrudRepository<InhousePart, Long> {
}
