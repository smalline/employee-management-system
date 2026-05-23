package com.smalline.ems.employee;

import java.time.Instant;
import java.time.LocalDate;

public record EmployeeResponse(
		Long id,
		String firstName,
		String lastName,
		String email,
		String jobTitle,
		LocalDate hireDate,
		Long departmentId,
		String departmentName,
		Instant createdAt,
		Instant updatedAt) {

	public static EmployeeResponse from(Employee employee) {
		return new EmployeeResponse(
				employee.getId(),
				employee.getFirstName(),
				employee.getLastName(),
				employee.getEmail(),
				employee.getJobTitle(),
				employee.getHireDate(),
				employee.getDepartment().getId(),
				employee.getDepartment().getName(),
				employee.getCreatedAt(),
				employee.getUpdatedAt());
	}
}
