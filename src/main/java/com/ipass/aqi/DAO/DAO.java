package com.ipass.aqi.DAO;

import com.ipass.aqi.webservices.AqiApiRequest;
import com.ipass.aqi.webservices.Updater;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DAO implements ServletContextListener {
    // Functie die wordt uitgevoerd bij starten server
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("Initializing Database...");
        AqiApiRequest req = new AqiApiRequest();

        // Hier kan worden gekozen om de table te wipen voordat de server word gestart
        //fillDB.delete("DROP TABLE aqi");

        // Probeer connectie te maken met de PostgreSQL DB
        try (Connection con = getConnection()) {

            // Check of de table AQI al bestaat, zo niet maak er een.
            // Als .isBeforeFirst() false returned betekent dat dat er geen resultaten zijn voor "aqi"
            // De table dus moet worden aangemaakt
            if (!con.getMetaData()
                    .getTables(null,null,"aqi", null)
                    .isBeforeFirst()) {
                System.out.println("AQI Table is being created");

                String query = "CREATE TABLE aqi("
                        + "city VARCHAR(255) NOT NULL," + "lat VARCHAR(255)," + "lon VARCHAR(255),"
                        + "aqi VARCHAR(255)," + "nameorg VARCHAR(255)," + "urlorg VARCHAR(255),"
                        + "co VARCHAR(255)," + "d VARCHAR(255)," + "h VARCHAR(255)," + "no2 VARCHAR(255),"
                        + "o3 VARCHAR(255)," + "p VARCHAR(255)," + "pm10 VARCHAR(255)," + "pm25 VARCHAR(255),"
                        + "so2 VARCHAR(255)," + "t VARCHAR(255)," + "w VARCHAR(255)," + "wd VARCHAR(255),"
                        + "displaytime VARCHAR(255)," + "timezone VARCHAR(255),"
                        + "PRIMARY KEY (city));";

                // Uitvoeren van de SQL query:
                con.createStatement().execute(query);
            }

            // Het aanmaken van 3 waarden in de table met de (Aqi)DAO
            // TODO maak customizable
            create(req.doGet("Paris"));
            create(req.doGet("Shanghai"));
            create(req.doGet("New York"));

            con.close();
            System.out.println("Connection closed!");

            // Hier wordt Updater gestart, een thread die om de dertig minuten de database update
            Updater updater = new Updater();
            updater.start();

            System.out.println("Update listener started.");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        System.out.println("Initialisation complete!");
    }

    private Connection getConnection() {

        // In het begin heeft de connectie nog geen waarde
        Connection result;

        try {
            // Hier moet de context worden geinitialiseerd, dit is voor het
            // lokaal runnen van deze applicatie niet nodig
            // Voor Heroku kan het echter belangrijk zijn, vermoedelijk heeft
            // Heroku hier momenteel problemen mee
            Properties props = new Properties();
            props.put(Context.SECURITY_PRINCIPAL, "hngiujnqgfyzhs");
            props.put(Context.SECURITY_CREDENTIALS, "d8d668942720a6934eaad11fdcdbe423fb79c6f17c92caf85bb99d3431c3d818");
            props.put(Context.PROVIDER_URL, "postgres://hngiujnqgfyzhs:d8d668942720a6934eaad11fdcdbe423fb79c6f17c92caf85bb99d3431c3d818@ec2-54-247-120-169.eu-west-1.compute.amazonaws.com:5432/d7gp1lhugl5fsn");

            InitialContext ic = new InitialContext();

            // Locatie van de Database
            DataSource ds = (DataSource) ic.lookup("java:comp/env/jdbc/PostgresDS");
            result = ds.getConnection();

            System.out.println("Connection opened!");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    // Deze functie maakt een lijst van de resultset van de query die als
    // parameter wordt meegegeven, de lijst is
    // te gebruiken zoals elke java lijst
    public List<AQIData> selectAqi(String query) {
        List<AQIData> results = new ArrayList<>();
        ResultSet dbResultSet = executeQuery(query);

        // Loop door alle resultaten heen
        try {
            while (dbResultSet.next()) {
                AQIData obj = new AQIData();
                obj.data.city.name = dbResultSet.getString("city");
                obj.data.city.geo[0] = dbResultSet.getString("lat");
                obj.data.city.geo[1] = dbResultSet.getString("lon");
                obj.data.aqi = dbResultSet.getString("aqi");
                obj.data.attributions[0].name = dbResultSet.getString("nameorg");
                obj.data.attributions[0].url = dbResultSet.getString("urlorg");
                obj.data.iaqi.co.v = dbResultSet.getString("co");
                obj.data.iaqi.d.v = dbResultSet.getString("d");
                obj.data.iaqi.h.v = dbResultSet.getString("h");
                obj.data.iaqi.no2.v = dbResultSet.getString("no2");
                obj.data.iaqi.o3.v = dbResultSet.getString("o3");
                obj.data.iaqi.p.v = dbResultSet.getString("p");
                obj.data.iaqi.pm10.v = dbResultSet.getString("pm");
                obj.data.iaqi.pm25.v = dbResultSet.getString("pm25");
                obj.data.iaqi.so2.v = dbResultSet.getString("so2");
                obj.data.iaqi.t.v = dbResultSet.getString("t");
                obj.data.iaqi.w.v = dbResultSet.getString("w");
                obj.data.iaqi.wd.v = dbResultSet.getString("wd");
                obj.data.time.s = dbResultSet.getString("displaytime");

                // opgeslagen resultaat aan lijst toevoegen
                results.add(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    // Een klasse die selectAqi start met alle data in de table aqi
    public List<AQIData> findAll() {
        return selectAqi("SELECT * FROM aqi");
    }

    private void create(AQIData aqi) {
        String query = "INSERT INTO aqi VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = getConnection()) {
            System.out.println("STRING: " + aqi.toString());
            System.out.println("Trying to create new row!");
            PreparedStatement statement = con.prepareStatement(query);
            statement = aqi.fillStatement(statement);
            statement.execute();
            System.out.println("Creation successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Een klasse die een row in de table aqi update
    public boolean update(AQIData aqi) {
        try (Connection con = getConnection()) {
            // De query werkt met een preparedstatement waar alle vraagtekens
            // worden aangevuld met de code onder de query
            String query = "UPDATE aqi SET city=?, lat=?, lon=?," +
                    "aqi=?, nameorg=?, urlorg=?, " +
                    "co=?, d=?, h=?, no2=?, o3=?, p=?, pm10=?, pm25=?, so2=?, t = ?, w=?, wd=?, " +
                    "displaytime=?, timezone = ? WHERE city='"
                    + aqi.data.city.name + "'";
            System.out.println("UPDATE aqi.data.city.name = " + aqi.data.city.name);

            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt = aqi.fillStatement(pstmt);
            pstmt.execute();
            con.close();
            System.out.println("Update Statement complete");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Verwijderen de complete aqi table
    public void delete(boolean drop) {
        // Bij deze functie wordt meegegeven of de table moet worden gedelete of
        // niet, als er false wordt meegegeven doet
        // deze functie dus eigenlijk niks
        if (drop) {
            executeQuery("DROP TABLE aqi");
        }
    }

    private ResultSet executeQuery(String query) {
        // Voer CRUD uit op Database
        try (Connection con = getConnection()) {
            System.out.println("Connected to database.");
            ResultSet result = con.createStatement().executeQuery(query);
            System.out.println("Statement executed: " + query);
            con.close();
            System.out.println("Connection to DB closed.");
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Functie die wordt uitgevoerd bij sluiten server
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Server shutting down... Good night");
    }
}
