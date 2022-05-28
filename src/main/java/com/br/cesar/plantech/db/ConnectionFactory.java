package com.br.cesar.plantech.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.br.cesar.plantech.PlantechApiApplication;

public class ConnectionFactory {
	public static java.sql.Connection getConnection() {
	Connection connection = null;

	    try {
	        /* Obtém o driver de conexão com o banco de dados */
//	        Class.forName("com.mysql.jdbc.Driver");
	    	Class.forName("com.mysql.cj.jdbc.Driver");

	        /* Configura os parâmetros da conexão */
	        String schema = "plantech";
	        String port = "3306";
	        String ip = "127.0.0.1";
	        String uri = "jdbc:mysql://"+ip+":"+port+"/"+schema;
//	        String url = "jdbc:mysql:ip:porta/schema";
	        String username = PlantechApiApplication.dotenv.get("DB_USER", "root"); 
	        String password =PlantechApiApplication.dotenv.get("DB_PASSWORD", "root");
	        
	        /* Tenta se conectar */
	        connection = DriverManager.getConnection(uri, username, password);

	        /* Caso a conexão ocorra com sucesso, a conexão é retornada */
	        return connection;
	        
	    } catch (ClassNotFoundException e) {            
	        System.out.println("O driver expecificado nao foi encontrado.");
	        return null;
	        
	    } catch (SQLException e) {
	        System.out.println("Nao foi possivel conectar ao banco de dados. "+ e);
	        return null;
	    }
	}
}
