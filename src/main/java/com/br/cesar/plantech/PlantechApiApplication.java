package com.br.cesar.plantech;

import com.br.cesar.plantech.db.ConnectionFactory;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	protected static Connection conn = null;

	public static void main(String[] args) { 
		try {
			dotenv = Dotenv.configure()
			        .directory("/opt/plantech/.env")
			        .ignoreIfMalformed()
			        .ignoreIfMissing()
			        .load();
			
			conn = ConnectionFactory.getConnection();
			
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
	
	@GetMapping("/get/questions")
	public Map<String, Object> getQuestions() throws SQLException {
		//json structure
//		Map<String, Object> questions = new LinkedHashMap<>();
//		Map<String, Object> question = new LinkedHashMap<>();
//		ArrayList<Map<String,String>> alternatives = new ArrayList<>();
//		Map<String,String> alternative = new LinkedHashMap<>();

//		Connection conn = ConnectionFactory.getConnection();
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

	@PostMapping("/find/response")
	public Map<String, String> findResponse(@RequestBody Map<String, Object> body) throws SQLException {
		
		ArrayList<Map<String,String>> responsesQuestions = (ArrayList<Map<String, String>>) body.get("questions");
		int[] amountResultPlants = new int[4];

		for (Map<String,String> responses : responsesQuestions) {
			//String idQuestions = responses.get("id_question");
			String response = responses.get("response");

			switch (response) {
				case ("1"):
					amountResultPlants[0]++;

				case ("2"):
					amountResultPlants[1]++;

				case ("3"):
					amountResultPlants[2]++;

				case ("4"):
					amountResultPlants[3]++;

				default:

			}
		}
		
		int finalValue = ResponseCalc.findResponse(amountResultPlants);
		
		assert conn != null;

		Statement stmt = conn.createStatement();
		String sqlPlant = "SELECT * FROM plant where id = '"+(finalValue+1)+"';";
		ResultSet resultPlants = stmt.executeQuery(sqlPlant);
		
		String idPlant = resultPlants.getString("id");
		String namePlant = resultPlants.getString("name");
		String scientificNamePlant = resultPlants.getString("other");
		String describePlant = resultPlants.getString("describe_is");
		String imagePlant = resultPlants.getString("image");

		Map<String, String> plant = new LinkedHashMap<>();
		plant.put("id", idPlant);
		plant.put("name", namePlant);
		plant.put("scientificName", scientificNamePlant);
		plant.put("describe", describePlant);
		plant.put("image", imagePlant);

		return plant;
	}
}
