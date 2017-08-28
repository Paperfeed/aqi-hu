package com.ipass.aqi.DAO;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

// Deze complete klasse is simpelweg een POD voor opslag van data vanuit Gson
public class AQIData {
    public String status;
    public Data data;

    public HashMap getData() {
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("city", this.data.city.name);
        hashMap.put("lat", this.data.city.geo[0]);
        hashMap.put("lon", this.data.city.geo[1]);
        hashMap.put("aqi", this.data.aqi);
        hashMap.put("nameorg", this.data.attributions[0].name);
        hashMap.put("urlorg", this.data.attributions[0].url);
        hashMap.put("co", this.data.iaqi.co.v);
        hashMap.put("h", this.data.iaqi.h.v);
        hashMap.put("no2", this.data.iaqi.no2.v);
        hashMap.put("o3", this.data.iaqi.o3.v);
        hashMap.put("p", this.data.iaqi.p.v);

        hashMap.put("pm10", this.data.iaqi.pm10.v);
        hashMap.put("pm25", this.data.iaqi.pm25.v);
        hashMap.put("so2", this.data.iaqi.so2.v);
        hashMap.put("t", this.data.iaqi.t.v);
        hashMap.put("displaytime", this.data.time.v);

        return hashMap;
    }

    public void fillStatement (PreparedStatement statement) {
        // Deze method vult de bijgeleverde PreparedStatement in met de data van het object
        try {
            statement.setString(1, this.data.city.name);
            statement.setString(2, this.data.city.geo[0]);
            statement.setString(3, this.data.city.geo[1]);
            statement.setString(4, this.data.aqi);
            statement.setString(5, this.data.attributions[0].name);
            statement.setString(6, this.data.attributions[0].url);
            statement.setString(7, this.data.iaqi.co.v);
            statement.setString(8, this.data.iaqi.h.v);
            statement.setString(9, this.data.iaqi.no2.v);
            statement.setString(10, this.data.iaqi.o3.v);
            statement.setString(11, this.data.iaqi.p.v);
            statement.setString(12, this.data.iaqi.pm10.v);
            statement.setString(13, this.data.iaqi.pm25.v);
            statement.setString(14, this.data.iaqi.so2.v);
            statement.setString(15, this.data.iaqi.t.v);
            statement.setString(16, this.data.time.v);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public class Data {
        public String aqi;
        public String idx;
        public Attributions[] attributions;
        public City city;
        public String dominentpol;
        public Iaqi iaqi;
        public Time time;
    }
    
    public class Attributions {
        public String name;
        public String url;
    }

    public class City {
        public String[] geo;
        public String name;
        public String url;
    }

    public class Iaqi {
        public AQIValue co;
        public AQIValue d;
        public AQIValue h;
        public AQIValue no2;
        public AQIValue o3;
        public AQIValue p;
        public AQIValue pm10;
        public AQIValue pm25;
        public AQIValue so2;
        public AQIValue t;
        public AQIValue w;
        public AQIValue wd;

    }

    public class AQIValue { public String v; }

    public final class Time
    {
        public String v;
        public String tz;
        public String s;

        @Override
        public String toString() {
            return this.s + " " + this.tz + " GMT";
        }
    }
}