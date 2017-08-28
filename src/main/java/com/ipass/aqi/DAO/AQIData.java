package com.ipass.aqi.DAO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

// Deze complete klasse is simpelweg een POD voor opslag van data vanuit Gson
public class AQIData {
    public String status;
    public Data data;

    public PreparedStatement fillStatement (PreparedStatement statement) {
        // Deze method vult de bijgeleverde PreparedStatement in met de data van het object
        try {
            statement.setString(1, this.data.city.name);
            statement.setString(2, this.data.city.geo[0]);
            statement.setString(3, this.data.city.geo[1]);
            statement.setString(4, this.data.aqi);
            statement.setString(5, this.data.attributions[0].name);
            statement.setString(6, this.data.attributions[0].url);
            statement.setString(7, this.data.iaqi.co.v);
            statement.setString(8, "");//this.data.iaqi.d.v);
            statement.setString(9, this.data.iaqi.h.v);
            statement.setString(10, this.data.iaqi.no2.v);
            statement.setString(11, this.data.iaqi.o3.v);
            statement.setString(12, this.data.iaqi.p.v);
            statement.setString(13, this.data.iaqi.pm10.v);
            statement.setString(14, this.data.iaqi.pm25.v);
            statement.setString(15, this.data.iaqi.so2.v);
            statement.setString(16, this.data.iaqi.t.v);
            statement.setString(17, "");//this.data.iaqi.w.v);
            statement.setString(18, "");//this.data.iaqi.wd.v);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statement;
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
            return s + " " + tz + " GMT";
        }
    }
}