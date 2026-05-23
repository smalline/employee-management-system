package com.smalline.ems.department;

import java.time.Instant;

public record DepartmentResponse(
		Long id,
		String name,
		String description,
		Instant createdAt,
		Instant updatedAt) {

	public static DepartmentResponse from(Department department) {
		return new DepartmentResponse(
				department.getId(),
				department.getName(),
				department.getDescription(),
				department.getCreatedAt(),
				department.getUpdatedAt());
	}
}
