package com.example.inventoryservice;

import com.example.inventoryservice.Repository.InventoryRepo;
import com.example.inventoryservice.model.Inventory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication

public class InventoryServiceApplication {
	private final InventoryRepo inventoryRepo;

	public InventoryServiceApplication(InventoryRepo inventoryRepo) {
		this.inventoryRepo = inventoryRepo;
	}

	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApplication.class, args);

	}

	@Bean
	public CommandLineRunner commandLineRunner() {
		return args ->
		{
			inventoryRepo.save(new Inventory(1L, "IPHONE 13", 50));
			inventoryRepo.save(new Inventory(2L, "IPHONE 15",100 ));
			inventoryRepo.save(new Inventory(3L, "SAMSUNG S23",200));
		};


//	}hi there
	}
}


