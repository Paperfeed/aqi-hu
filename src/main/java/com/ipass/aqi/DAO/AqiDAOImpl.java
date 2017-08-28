package com.ipass.aqi.DAO;

import com.ipass.aqi.webservices.AqiApiRequest;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class AqiDAOImpl implements AqiDAO {
    //list is working as a database
    private List<AQIData> aqiDataList;

    public AqiDAOImpl(){
        aqiDataList = new ArrayList<AQIData>();
        Connection conn = getConnection();

        AqiApiRequest aqiApiRequest = new AqiApiRequest();
        try {
            AQIData paris = aqiApiRequest.doGet("Paris");
            AQIData shanghai = aqiApiRequest.doGet("Shanghai");
            AQIData newyork = aqiApiRequest.doGet("New York");
            aqiDataList.add(paris);
            aqiDataList.add(shanghai);
            aqiDataList.add(newyork);
            addToDB(paris);
            addToDB(shanghai);
            addToDB(newyork);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initDAO () {
        try (Connection con = getConnection()) {
            // Check of de table AQI al bestaat, zo niet maak er een.
            // Als .isBeforeFirst() false returned betekent dat dat er geen resultaten zijn voor "aqi"
            // De table dus moet worden aangemaakt
            if (!con.getMetaData()
                    .getTables(null, null, "aqi", null)
                    .isBeforeFirst()) {
                System.out.println("AQI Table is being created");

                String query = "CREATE TABLE aqi("
                        + "city VARCHAR(255) NOT NULL," + "lat VARCHAR(255)," + "lon VARCHAR(255),"
                        + "aqi VARCHAR(255)," + "nameorg VARCHAR(255)," + "urlorg VARCHAR(255),"
                        + "co VARCHAR(255)," + "h VARCHAR(255)," + "no2 VARCHAR(255),"
                        + "o3 VARCHAR(255)," + "p VARCHAR(255)," + "pm10 VARCHAR(255)," + "pm25 VARCHAR(255),"
                        + "so2 VARCHAR(255)," + "t VARCHAR(255),"
                        + "displaytime VARCHAR(255),"
                        + "PRIMARY KEY (city));";

                // Uitvoeren van de SQL query:
                con.createStatement().execute(query);
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AQIData> getAllData() {
        return this.aqiDataList;
    }

    @Override
    public void updateAllData() {
        for (AQIData aqiData : aqiDataList) {
            updateData(aqiData);
        }
    }

    @Override
    public AQIData getData(String search) {
        for (AQIData aqiData : aqiDataList) {
            if (Objects.equals(aqiData.data.city.name, search)) {
                // Data bestaat al, stuur deze terug.
                return aqiData;
            }
        }

        // Data bestaat nog niet, stuur door naar ApiApiRequest
        return retrieveFromAPI(search);
    }

    private AQIData retrieveFromAPI(String search) {
        AqiApiRequest aqiApiRequest = new AqiApiRequest();
        try {
            AQIData result = aqiApiRequest.doGet(search);
            aqiDataList.add(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AQIData updateData(AQIData aqiData) {
        AQIData update = retrieveFromAPI(aqiData.data.city.name);
        addToDB(update);
        return update;
    }

    @Override
    public void deleteData(AQIData aqiData) {
        for (AQIData obj : aqiDataList) {
            if (Objects.equals(aqiData.data.city.name, obj.data.city.name)) {
                aqiDataList.remove(obj);
            }
        }
    }

    private Connection getConnection() {
        // In het begin heeft de connectie nog geen waarde
        Connection conn;

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
            conn = ds.getConnection();

            System.out.println("Connection opened!");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return conn;
    }

    // Deze functie maakt een lijst van de resultset van de query die als
    // parameter wordt meegegeven, de lijst is
    // te gebruiken zoals elke java lijst
    public List<AQIData> selectAqi(String query) {
        List<AQIData> results = new ArrayList<>();
        try (ResultSet dbResultSet = executeQuery(query)) {
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
            // De opgevraagde informatie bestaat niet of een andere fout is voorkomen
        }

        return results;
    }

    // Een klasse die selectAqi start met alle data in de table aqi
    public List<AQIData> findAll() {
        return selectAqi("SELECT * FROM aqi");
    }

    private void addToDB(AQIData aqi) {
        try (Connection conn = getConnection()) {
            // Check if entry exists already, if so, then update instead.
            String sql = "SELECT 1 FROM aqi WHERE city = '" + aqi.data.city.name +"';";
            if (conn.prepareStatement(sql).execute()) {
                System.out.println("Entry already exists.");
                sql = "UPDATE aqi SET city = ?, lat = ?, lon = ?," +
                        "aqi = ?, nameorg = ?, urlorg = ?, " +
                        "co = ?, h = ?, no2 = ?, o3 = ?, p = ?, pm10 = ?, pm25 = ?, so2 = ?, t = ?, " +
                        "displaytime = ? WHERE city='" + aqi.data.city.name + "';";
            } else {
                sql = "INSERT INTO aqi (city, lat, lon, aqi, nameorg, urlorg, " +
                        "co, h, no2, o3, p, pm10, pm25, so2, t, displaytime) " +
                        "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );";
            }

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            aqi.fillStatement(preparedStatement);
            preparedStatement.execute();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

/*       String query = "INSERT INTO " +
                "aqi (city, lat, lon, " +
                "aqi, nameorg, urlorg, " +
                "co, h, no2, o3, p, pm10, pm25, so2, t) " +
                "VALUES('" +
                data.get("city") + "','" +
                data.get("lat") + "','" +
                data.get("lon") + "','" +
                data.get("aqi")  + "','" +
                data.get("nameorg")  + "','" +
                data.get("urlorg")  + "','" +
                data.get("co")  + "','" +
                data.get("h")  + "','" +
                data.get("no2")  + "','" +
                data.get("o3")  + "','" +
                data.get("p")  + "','" +
                data.get("pm10")  + "','" +
                data.get("pm25")  + "','" +
                data.get("so2")  + "','" +
                data.get("t")  + "');";

        System.out.println("query = " + query);
        executeQuery(query);*/

        /*String query = "INSERT INTO aqi VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = getConnection()) {
            System.out.println("STRING: " + aqi.toString());
            System.out.println("Trying to create new row!");
            PreparedStatement statement = con.prepareStatement(query);
            statement = aqi.fillStatement(statement);
            statement.execute();
            System.out.println("Creation successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }
    
    // Een klasse die een row in de table aqi update
    public boolean update(AQIData aqi) {
        try (Connection con = getConnection()) {
            // De query werkt met een preparedstatement waar alle vraagtekens
            // worden aangevuld met de code onder de query
            String query = "UPDATE aqi SET city=?, lat=?, lon=?," +
                    "aqi=?, nameorg=?, urlorg=?, " +
                    "co=?, d=?, h=?, no2=?, o3=?, p=?, pm10=?, pm25=?, so2=?, t = ?, " +
                    "displaytime=? WHERE city='"
                    + aqi.data.city.name + "';";
            System.out.println("UPDATE aqi.data.city.name = " + aqi.data.city.name);

            PreparedStatement pstmt = con.prepareStatement(query);
            aqi.fillStatement(pstmt);
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
    public void deleteTable() {
        System.out.println("Deleting database...");
        executeQuery("DROP TABLE aqi");
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
}
