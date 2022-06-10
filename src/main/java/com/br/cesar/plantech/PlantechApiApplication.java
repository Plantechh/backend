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

		return String.format("Hello %s!", name);
	}
	
	@GetMapping("/Questions")
	public Map<String, Object> questions() throws SQLException {
		//json structure
//		Map<String, Object> questions = new LinkedHashMap<>();
//		Map<String, Object> question = new LinkedHashMap<>();
//		ArrayList<Map<String,String>> alternatives = new ArrayList<>();
//		Map<String,String> alternative = new LinkedHashMap<>();

		Connection conn = ConnectionFactory.getConnection();
		assert conn != null;

		Statement stmt = conn.createStatement();
		String sqlQuestions = "SELECT * FROM questions";
		ResultSet resultQuestions = stmt.executeQuery(sqlQuestions);

		ArrayList<Object> questions = new ArrayList<>();
		while (resultQuestions.next()) {
			String idQuestion = resultQuestions.getString("id");
			String textQ = resultQuestions.getString("text_is");
			String group_responses = resultQuestions.getString("id_responses");

			String sqlAlternatives = "SELECT * FROM responses WHERE group_at = " + idQuestion;
			Statement stmtAl = conn.createStatement();
			ResultSet resultAlternatives = stmtAl.executeQuery(sqlAlternatives);
			ArrayList<Map<String,String>> alternatives = new ArrayList<>();
			while (resultAlternatives.next()) {
				Map<String, String> alternative = new LinkedHashMap<>();

				alternative.put("id", resultAlternatives.getString("id"));
				alternative.put("title", resultAlternatives.getString("text_is"));
				alternative.put("points", resultAlternatives.getString("point_at"));
				alternatives.add(alternative);
			}

			Map<String,Object> question = new LinkedHashMap<>();
			question.put("id", idQuestion);
			question.put("title", textQ);
			question.put("alternatives",alternatives);
			questions.add(question);
		}
		Map<String,Object> finalJson = new LinkedHashMap<>();
		finalJson.put("questions", questions);
		return finalJson;
	}
}
