package com.smalline.ems.employee;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smalline.ems.common.DuplicateResourceException;
import com.smalline.ems.common.ResourceNotFoundException;
import com.smalline.ems.department.Department;
import com.smalline.ems.department.DepartmentService;

@Service
@Transactional
public class EmployeeService {

	private final EmployeeRepository employeeRepository;
	private final DepartmentService departmentService;

	public EmployeeService(EmployeeRepository employeeRepository, DepartmentService departmentService) {
		this.employeeRepository = employeeRepository;
		this.departmentService = departmentService;
	}

	@Transactional(readOnly = true)
	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Employee findById(Long id) {
		return employeeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found with id " + id));
	}

	public Employee create(String firstName, String lastName, String email, String jobTitle, LocalDate hireDate,
			Long departmentId) {
		if (employeeRepository.existsByEmailIgnoreCase(email)) {
			throw new DuplicateResourceException("Employee already exists with email " + email);
		}

		Department department = departmentService.findById(departmentId);
		Employee employee = new Employee(firstName, lastName, email, jobTitle, hireDate, department);
		return employeeRepository.save(employee);
	}

	public Employee update(Long id, String firstName, String lastName, String email, String jobTitle, LocalDate hireDate,
			Long departmentId) {
		Employee employee = findById(id);

		employeeRepository.findByEmailIgnoreCase(email)
				.filter(existing -> !existing.getId().equals(id))
				.ifPresent(existing -> {
					throw new DuplicateResourceException("Employee already exists with email " + email);
				});

		Department department = departmentService.findById(departmentId);
		employee.setFirstName(firstName);
		employee.setLastName(lastName);
		employee.setEmail(email);
		employee.setJobTitle(jobTitle);
		employee.setHireDate(hireDate);
		employee.setDepartment(department);
		return employee;
	}

	public void delete(Long id) {
		Employee employee = findById(id);
		employeeRepository.delete(employee);
	}
}
