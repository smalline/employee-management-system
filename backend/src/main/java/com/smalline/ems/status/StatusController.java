package com.smalline.ems.status;

import java.time.Instant;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

	@GetMapping("/api/status")
	public Map<String, Object> status() {
		return Map.of(
				"status", "UP",
				"service", "employee-management-backend",
				"timestamp", Instant.now().toString());
	}
}
