package com.erp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"com.erp.server", "infra"})
@EnableJpaRepositories(basePackages = "infra.global.relational.repositories")
@EntityScan(basePackages = "infra.global.relational.entities")
@EnableMongoRepositories(
        basePackages = "infra.global.document.repositories",
        mongoTemplateRef = "mongoTemplate"
)
public class NexoErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexoErpApplication.class, args);
	}

}
