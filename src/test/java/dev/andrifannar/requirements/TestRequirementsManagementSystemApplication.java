package dev.andrifannar.requirements;

import org.springframework.boot.SpringApplication;

public class TestRequirementsManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(RequirementsManagementSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
