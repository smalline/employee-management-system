package com.smalline.ems.employee;

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
@RequestMapping("/api/employees")
public class EmployeeController {

	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping
	public List<EmployeeResponse> findAll() {
		return employeeService.findAll().stream()
				.map(EmployeeResponse::from)
				.toList();
	}

	@GetMapping("/{id}")
	public EmployeeResponse findById(@PathVariable Long id) {
		return EmployeeResponse.from(employeeService.findById(id));
	}

	@PostMapping
	public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request) {
		Employee employee = employeeService.create(
				request.firstName(),
				request.lastName(),
				request.email(),
				request.jobTitle(),
				request.hireDate(),
				request.departmentId());

		return ResponseEntity
				.created(URI.create("/api/employees/" + employee.getId()))
				.body(EmployeeResponse.from(employee));
	}

	@PutMapping("/{id}")
	public EmployeeResponse update(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {
		return EmployeeResponse.from(employeeService.update(
				id,
				request.firstName(),
				request.lastName(),
				request.email(),
				request.jobTitle(),
				request.hireDate(),
				request.departmentId()));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		employeeService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
