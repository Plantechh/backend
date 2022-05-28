package com.br.cesar.plantech;

import java.sql.Connection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.cesar.plantech.db.ConnectionFactory;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@RestController
public class PlantechApiApplication {
	public static Dotenv dotenv = null;

	public static void main(String[] args) { 
		try {
			dotenv = Dotenv.configure()
			        .directory("/opt/plantech/.env")
			        .ignoreIfMalformed()
			        .ignoreIfMissing()
			        .load();

			Connection conn = ConnectionFactory.getConnection();
			System.out.println("DB CONNECTION "+ conn.getCatalog());

			SpringApplication.run(PlantechApiApplication.class, args);
		}catch (Exception e) {
			System.out.println("Erro: "+ e);
		}
	}
	
	@GetMapping("/hello")
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}

}
