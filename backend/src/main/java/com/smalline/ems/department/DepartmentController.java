package com.smalline.ems.department;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

	private final DepartmentService departmentService;

	public DepartmentController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@GetMapping
	public List<DepartmentResponse> findAll() {
		return departmentService.findAll().stream()
				.map(DepartmentResponse::from)
				.toList();
	}

	@GetMapping("/{id}")
	public DepartmentResponse findById(@PathVariable Long id) {
		return DepartmentResponse.from(departmentService.findById(id));
	}

	@PostMapping
	public ResponseEntity<DepartmentResponse> create(@Valid @RequestBody DepartmentRequest request) {
		Department department = departmentService.create(request.name(), request.description());
		return ResponseEntity
				.created(URI.create("/api/departments/" + department.getId()))
				.body(DepartmentResponse.from(department));
	}

	@PutMapping("/{id}")
	public DepartmentResponse update(@PathVariable Long id, @Valid @RequestBody DepartmentRequest request) {
		return DepartmentResponse.from(departmentService.update(id, request.name(), request.description()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		departmentService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
