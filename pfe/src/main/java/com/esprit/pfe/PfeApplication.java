package com.esprit.pfe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan(basePackages ={
		"com.esprit.pfe.controller",
		"com.esprit.pfe.service",
		"com.esprit.pfe.repository",
		"com.esprit.pfe.auth",
		"com.esprit.pfe.config"


})
@EnableJpaRepositories(basePackages = {"com.esprit.pfe.repository"})
@EntityScan(basePackages = {"com.esprit.pfe.Entity"})
@EnableAspectJAutoProxy
public class PfeApplication {

	public static void main(String[] args) {
		SpringApplication.run(PfeApplication.class, args);
	}

}
