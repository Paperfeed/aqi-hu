package com.ipass.aqi.DAO;

import java.sql.Connection;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BaseDAO {
	protected final Connection getConnection() {
		// In het begin heeft de connectie nog geen waarde
		Connection result = null;
		try {
			// Hier moet de context worden geinitialiseerd, dit is voor het
			// lokaal runnen van deze applicatie niet nodig
			// Voor Heroku kan het echter belangrijk zijn, vermoedelijk heeft
			// Heroku hier momenteel problemen mee
			Properties props = new Properties();
			props.put(Context.SECURITY_PRINCIPAL, "hngiujnqgfyzhs");
			props.put(Context.SECURITY_CREDENTIALS, "d8d668942720a6934eaad11fdcdbe423fb79c6f17c92caf85bb99d3431c3d818");
			props.put(Context.PROVIDER_URL, "postgres://hngiujnqgfyzhs:d8d668942720a6934eaad11fdcdbe423fb79c6f17c92caf85bb99d3431c3d818@ec2-54-247-120-169.eu-west-1.compute.amazonaws.com:5432/d7gp1lhugl5fsn");
			//props.put(Context.INITIAL_CONTEXT_FACTORY, "test");
			InitialContext ic = new InitialContext();
			// Locatie van de Database
			DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/PostgresDS");
			result = ds.getConnection();
			System.out.println("Connection opened!");
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return result;
	}
}