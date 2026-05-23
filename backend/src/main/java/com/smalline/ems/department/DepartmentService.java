package com.smalline.ems.department;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smalline.ems.common.DuplicateResourceException;
import com.smalline.ems.common.ResourceNotFoundException;

@Service
@Transactional
public class DepartmentService {

	private final DepartmentRepository departmentRepository;

	public DepartmentService(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	@Transactional(readOnly = true)
	public List<Department> findAll() {
		return departmentRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Department findById(Long id) {
		return departmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Department not found with id " + id));
	}

	public Department create(String name, String description) {
		if (departmentRepository.existsByNameIgnoreCase(name)) {
			throw new DuplicateResourceException("Department already exists with name " + name);
		}

		return departmentRepository.save(new Department(name, description));
	}

	public Department update(Long id, String name, String description) {
		Department department = findById(id);

		departmentRepository.findByNameIgnoreCase(name)
				.filter(existing -> !existing.getId().equals(id))
				.ifPresent(existing -> {
					throw new DuplicateResourceException("Department already exists with name " + name);
				});

		department.setName(name);
		department.setDescription(description);
		return department;
	}

	public void delete(Long id) {
		Department department = findById(id);
		departmentRepository.delete(department);
	}
}
