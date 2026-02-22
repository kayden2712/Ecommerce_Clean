package com.example.ecommerce_clean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;

@Modulithic(sharedModules = {"shared", "common"})
@SpringBootApplication
public class EcommerceCleanApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceCleanApplication.class, args);
	}

}
