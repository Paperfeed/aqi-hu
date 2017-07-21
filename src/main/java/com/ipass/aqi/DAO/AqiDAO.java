package com.ipass.aqi.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// DAO voor het uitvoeren van CRUD op de database, in het bijzonder het updaten van de reference tables.
public class AqiDAO extends BaseDAO {
	// Deze functie maakt een lijst van de resultset van de query die als
	// parameter wordt meegegeven, de lijst is
	// te gebruiken zoals elke java lijst
	private List<Aqi> selectAqi(String query) {
		List<Aqi> results = new ArrayList<Aqi>();

		try (Connection con = super.getConnection()) {
			PreparedStatement pstmt = con.prepareStatement(query);
			ResultSet dbResultSet = pstmt.executeQuery();
			// Loop door alle resultaten heen
			while (dbResultSet.next()) {
				String city = dbResultSet.getString("city");
				String aqif = dbResultSet.getString("aqif");
				String no2 = dbResultSet.getString("no2");
				String p = dbResultSet.getString("p");
				String o3 = dbResultSet.getString("o3");
				String pm25 = dbResultSet.getString("pm25");
				String t = dbResultSet.getString("t");
				String so2 = dbResultSet.getString("so2");
				String h = dbResultSet.getString("h");
				String pm = dbResultSet.getString("pm");
				String co = dbResultSet.getString("co");
				String wd = dbResultSet.getString("wd");
				String nameorg = dbResultSet.getString("nameorg");
				String displaytime = dbResultSet.getString("displaytime");
				String urlorg = dbResultSet.getString("urlorg");
				String longitude = dbResultSet.getString("longitude");
				String latitude = dbResultSet.getString("latitude");
				// resultaat in pojo opslaan
				Aqi newAqi = new Aqi(city, aqif, no2, p, o3, pm25, t, so2, h, pm, co, wd, nameorg, displaytime, urlorg,
						longitude, latitude);
				// opgeslagen resultaat aan lijst toevoegen
				results.add(newAqi);
			}
			con.close();
			System.out.println("Connection closed! SELECT Statement complete");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		// Return van alle resultaten in een lijst
		return results;
	}

	// Een klasse die selectAqi start met alle data in de table aqi
	public List<Aqi> findAll() {
		return selectAqi("SELECT * FROM aqi");
	}

	// Een klasse die een row in de table aqi update
	public Aqi update(Aqi aqi) {
		Aqi res = null;

		try (Connection con = super.getConnection()) {
			// Een belangrijke uitleg: De onderstaande variablen zijn allemaal
			// objecten omdat de API niet altijd dezelde
			// datatypen returned, soms is een waarde bijvoorbeeld een long in
			// plaats van een double
			// hierdoor is de waarden opslaan in een object een makkelijke
			// oplossing
			Object city = aqi.getCity();
			Object aqif = aqi.getAqif();
			Object no2 = aqi.getNo2();
			Object p = aqi.getP();
			Object o3 = aqi.getO3();
			Object pm25 = aqi.getPm25();
			Object t = aqi.getT();
			Object so2 = aqi.getSo2();
			Object h = aqi.getH();
			Object pm = aqi.getPm();
			Object co = aqi.getCo();
			Object wd = aqi.getWd();
			Object nameorg = aqi.getNameorg();
			Object displaytime = aqi.getDisplaytime();
			Object urlorg = aqi.getUrlorg();
			Object longitude = aqi.getLongitude();
			Object latitude = aqi.getLatitude();

			// De query werkt met een preparedstatement waar alle vraagtekens
			// worden aangevuld met de code onder de query
			String query = "UPDATE aqi SET city = ?, aqif = ?, no2 = ?, p = ?, o3 = ?, pm25 = ?, t = ?, so2 = ?, h = ?, pm = ?, co = ?, wd = ?, nameorg = ?, displaytime = ?, urlorg = ?, longitude = ?, latitude = ? WHERE city = '"
					+ (String) city + "'";

			PreparedStatement pstmt = con.prepareStatement(query);
			// De cijfers in de lijnen code hieronder representeren het
			// vraagteken waar de variable geplaatst moet worden
			// Voor het gemak en het feit dat de API niet consequent dezelfde
			// datatypen levert heb ik alle data als String
			// in de database bewaard
			pstmt.setString(1, (String) city);
			pstmt.setString(2, String.valueOf(aqif));
			pstmt.setString(3, String.valueOf(no2));
			pstmt.setString(4, String.valueOf(p));
			pstmt.setString(5, String.valueOf(o3));
			pstmt.setString(6, String.valueOf(pm25));
			pstmt.setString(7, String.valueOf(t));
			pstmt.setString(8, String.valueOf(so2));
			pstmt.setString(9, String.valueOf(h));
			pstmt.setString(10, String.valueOf(pm));
			pstmt.setString(11, String.valueOf(co));
			pstmt.setString(12, String.valueOf(wd));
			pstmt.setString(13, (String) nameorg);
			pstmt.setString(14, (String) displaytime);
			pstmt.setString(15, (String) urlorg);
			pstmt.setString(16, String.valueOf(longitude));
			pstmt.setString(17, String.valueOf(latitude));
			System.out.println("Query for update: " + query);
			// uitvoeren SQL query
			pstmt.execute();
			con.close();
			System.out.println("Connection closed! Update Statement complete");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
		return res;
	}

	// Verwijderen de complete aqi table
	public void delete(boolean drop) {
		// Bij deze functie wordt meegegeven of de table moet worden gedelete of
		// niet, als er false wordt meegegeven doet
		// deze functie dus eigenlijk niks
		if (drop) {
			try (Connection con = super.getConnection()) {
				// Hetzelfde als de functies hierboven, uitvoeren van een SQL
				// query/statement
				String query = "DROP TABLE aqi";
				Statement stmt = con.createStatement();
				stmt.execute(query);
				con.close();
				System.out.println("Connection closed! Delete Statement complete");
				System.out.println("'aqi' Table deleted!");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// Het aanmaken van een table row, dit hoeft maar eenmalig aangezien de
	// Updater klasse vervolgens dezelfde rows onderhoud
	public void create(Aqi aqi) {
		try (Connection con = super.getConnection()) {
			// Een belangrijke uitleg: De onderstaande variablen zijn allemaal
			// objecten omdat de API niet altijd dezelde
			// datatypen returned, soms is een waarde bijvoorbeeld een long in
			// plaats van een double
			// hierdoor is de waarden opslaan in een object een makkelijke
			// oplossing
			Object city = aqi.getCity();
			Object aqif = aqi.getAqif();
			Object no2 = aqi.getNo2();
			Object p = aqi.getP();
			Object o3 = aqi.getO3();
			Object pm25 = aqi.getPm25();
			Object t = aqi.getT();
			Object so2 = aqi.getSo2();
			Object h = aqi.getH();
			Object pm = aqi.getPm();
			Object co = aqi.getCo();
			Object wd = aqi.getWd();
			Object nameorg = aqi.getNameorg();
			Object displaytime = aqi.getDisplaytime();
			Object urlorg = aqi.getUrlorg();
			Object longitude = aqi.getLongitude();
			Object latitude = aqi.getLatitude();

			// Uitvoeren van een SQL query/statement
			String query = "INSERT INTO aqi (city, aqif, no2, p, o3, pm25, t, so2, h, pm, co, wd, nameorg, displaytime, urlorg, longitude, latitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, (String) city);
			pstmt.setString(2, String.valueOf(aqif));
			pstmt.setString(3, String.valueOf(no2));
			pstmt.setString(4, String.valueOf(p));
			pstmt.setString(5, String.valueOf(o3));
			pstmt.setString(6, String.valueOf(pm25));
			pstmt.setString(7, String.valueOf(t));
			pstmt.setString(8, String.valueOf(so2));
			pstmt.setString(9, String.valueOf(h));
			pstmt.setString(10, String.valueOf(pm));
			pstmt.setString(11, String.valueOf(co));
			pstmt.setString(12, String.valueOf(wd));
			pstmt.setString(13, (String) nameorg);
			pstmt.setString(14, (String) displaytime);
			pstmt.setString(15, (String) urlorg);
			pstmt.setString(16, String.valueOf(longitude));
			pstmt.setString(17, String.valueOf(latitude));
			System.out.println("Query for create: " + query);
			pstmt.execute();
			con.close();
			System.out.println("Connection closed! Create Statement complete");
		} catch (SQLException sqle) {
			sqle.printStackTrace();
		}
	}
}
