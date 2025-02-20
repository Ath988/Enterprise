package com.bilgeadam.repository;

import com.bilgeadam.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, String> {
	
	List<Option> findAllByIdIn(List<String> ids);
}