package com.smalline.ems.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Override
	@EntityGraph(attributePaths = "department")
	List<Employee> findAll();

	@Override
	@EntityGraph(attributePaths = "department")
	Optional<Employee> findById(Long id);

	boolean existsByEmailIgnoreCase(String email);

	Optional<Employee> findByEmailIgnoreCase(String email);
}
