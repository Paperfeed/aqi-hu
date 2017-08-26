package com.ipass.aqi.DAO;

import com.ipass.aqi.webservices.AqiApiRequest;
import com.ipass.aqi.webservices.Updater;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.sql.*;

// De weblistener notatie sorgt ervoor dat de klasse tijdens het opstarten van Tomcat eenmalig wordt opgestart
// Hiervoor moet hij ook de ServletContextListener implementen
@WebListener
public class initDAO extends BaseDAO implements ServletContextListener {

	// Functie die wordt uitgevoerd bij starten server
	public void contextInitialized(ServletContextEvent event) {

		System.out.println("Initializing Database... Please wait");
		AqiDAO fillDB = new AqiDAO();
		AqiApiRequest req = new AqiApiRequest();

		// Hier kan worden gekozen om de table te wipen voordat de server wordt
		// gestart
		// Standaard staat deze functie aan omdat er in principe niet heel veel
		// belang is om data op lange termijn te
		// bewaren momenteel
		fillDB.delete(true);

		// De klasse probeert connectie te maken door de superklasse (BaseDAO)
		// aan te roepen
		try (Connection con = super.getConnection()) {
			// De volgende lijnen code zijn een simpele check om te kijken of de
			// table aqi al bestaat, als de table niet
			// bestaat maakt deze klasse de table aqi aan
			boolean createtable = true;
			DatabaseMetaData md = con.getMetaData();
			ResultSet rs = md.getTables(null, null, "%", null);
			while (rs.next()) {
				if (rs.getString(3).equals("aqi")) {
					createtable = false;
				}
			}
			System.out.println("Java has determined that createtable is: " + createtable);
			// Hier eindigt de check of de table bestaat
			// True: table moet worden gemaakt, False: table bestaat al en de
			// code wordt overgeslagen
			if (createtable) {
				String query = "CREATE TABLE aqi(" + "city VARCHAR(255) NOT NULL," + "aqif VARCHAR(255),"
						+ "no2 VARCHAR(255)," + "p VARCHAR(255)," + "o3 VARCHAR(255)," + "pm25 VARCHAR(255),"
						+ "t VARCHAR(255)," + "so2 VARCHAR(255)," + "h VARCHAR(255)," + "pm VARCHAR(255),"
						+ "co VARCHAR(255)," + "wd VARCHAR(255)," + "nameorg VARCHAR(255),"
						+ "displaytime VARCHAR(255)," + "urlorg VARCHAR(255)," + "longitude VARCHAR(255),"
						+ "latitude VARCHAR(255)," + "PRIMARY KEY (city));";
				PreparedStatement pstmt = con.prepareStatement(query);
				// Uitvoeren van de SQL query met een preparedstatement
				boolean res = pstmt.execute();
				System.out.println("Created table: " + res);
			}
			// Het aanmaken van 3 waarden in de table met de (Aqi)DAO
			fillDB.create(req.doGet("Paris"));
			fillDB.create(req.doGet("Berlin"));
			fillDB.create(req.doGet("New York"));
			con.close();
			System.out.println("Connection closed! (InitDAO)");

			// Hier wordt Updater gestart, een thread die eens om de zoveel tijd
			// (30 minuten) een database update uitvoert
			Updater a = new Updater();
			// "Updater" thread starten
			a.start();
			System.out.println("Update listener started!");

		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("Initialisation complete!");
	}

	// Functie die wordt uitgevoerd bij sluiten server
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("Server shutting down...");
	}
}
