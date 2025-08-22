package com.erp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.erp.server", "infra"})
@EnableJpaRepositories(basePackages = "infra.global.repositories")
@EntityScan(basePackages = "infra.global.entities")
public class NexoErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexoErpApplication.class, args);
	}

}
