package com.ipass.aqi.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.gson.*;

import com.ipass.aqi.DAO.AQIData;

public class AqiApiRequest {

	// De functie die bij een get wordt gestart
	public AQIData doGet(String search) throws IOException {
		// Uniek token die de WAQI API nodig heeft om een resultaat te kunnen geven
		String token = "3817177d29c5845192f55c3a2a4ca92fe16e93ee";

		URL waqi = new URL("https://api.waqi.info/feed/" + search + "/?token=" + token);
		// Test URL: https://api.waqi.info/feed/Utrecht/?token=3817177d29c5845192f55c3a2a4ca92fe16e93ee
		//
		// Je kan per Lat,Lon zoeken dmv geo:[lat];[lon] als search te gebruiken
		// of per zoekterm met /search/?keyword=[ZOEKTERM]&token=[TOKEN]
		// Amsterdam lat,lon: 52.3702;4.8952

		// TODO verbeter readen van JSON
        // Open connectie naar de WAQI API en haal de juiste informatie
		// lees het daarna uit als string met BufferedReader
		URL url = new URL("https://api.waqi.info/feed/" + search + "/?token=" + token);
		System.out.println(url);
		URLConnection yc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				yc.getInputStream()));

		//Initialiseren van waarden
        String inputLine, result = "";

        while ((inputLine = in.readLine()) != null) {
            result = result + inputLine;
        }

        // De JSon is opgehaald er word nu dmv Gson tot object deserialized
        Gson gson = new Gson();
        AQIData obj = gson.fromJson(result, AQIData.class);

		// TODO voeg code toe voor als het niet is gelukt
        if (obj.status.equals("ok")) {
			System.out.println("Gson has been deserialized into object");
		}

		return obj;
	}
}

