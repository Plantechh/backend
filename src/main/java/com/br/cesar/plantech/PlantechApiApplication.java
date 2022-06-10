package com.br.cesar.plantech;

import com.br.cesar.plantech.db.ConnectionFactory;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

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
	public String hello(@RequestParam(value = "name", defaultValue = "World") String name) throws SQLException {
		Connection conn = ConnectionFactory.getConnection();
		assert conn != null;
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM questions";
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			System.out.println(rs.getString("text_is"));
		}
		return String.format("Hello %s!", name);
	}
	
	@GetMapping("/Questions")
	public Map<String, Object> questions() {
		//json structure
		Map<String, Object> questions = new LinkedHashMap<>();
		Map<String, Object> question = new LinkedHashMap<>();
		ArrayList<Map<String,String>> alternatives = new ArrayList<>();
		Map<String,String> alternative = new LinkedHashMap<>();


		return questions;
	}
}
