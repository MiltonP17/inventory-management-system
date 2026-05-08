package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.domain.OutsourcedPart;

public interface OutsourcedPartRepository extends CrudRepository<OutsourcedPart, Long> {
}
