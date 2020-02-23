package io.weidongxu.webapp.textnormalization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AzResourceManagement {

	public static void main(String[] args) {
		AzureManagement azureMgmt = new AzureManagement();

		SpringApplication.run(AzResourceManagement.class, args);
	}
}
