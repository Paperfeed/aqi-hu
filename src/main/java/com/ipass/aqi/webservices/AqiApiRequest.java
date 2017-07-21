package com.ipass.aqi.webservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ipass.aqi.DAO.Aqi;

public class AqiApiRequest {
	// De functie die bij een get wordt gestart
	public Aqi doGet(String req) throws IOException{
		// Uniek token die de API nodig heeft om een resultaat te kunnen geven
		String token = "3817177d29c5845192f55c3a2a4ca92fe16e93ee";		
        String search = req;
        //Het openen van de connectie, bij de url worden de user-input en token toegevoegd, vervolgens wordt de connectie geopent, 
        // gelezen en veranderd naar een bufferedreader.
		URL oracle = new URL("https://api.waqi.info/feed/" + search + "/?token=" + token);
		System.out.println(oracle);
		URLConnection yc = oracle.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(
                yc.getInputStream()));
		 
		//Initialiseren van waarden
        String inputLine;
        String result = "";
      
        // Hier worden alle lines van de while loop in result opgeslagen, dit is 
        // nodig aangezien inputline alleen de waarde van de laatst gelezen line heeft.
        // Het resultaat is een String, deze String moet nog naar een JsonObject worden omgezet.
        while ((inputLine = in.readLine()) != null)
            result = result + inputLine;
        	System.out.println(inputLine);
        	
        // De try catch is om parseexceptions op te vangen.
        JSONObject jsonObject = null;
		try {
			jsonObject = (JSONObject) new JSONParser().parse(result);
			System.out.println(jsonObject);
		} 
		catch (ParseException e) {
			e.printStackTrace();
		}

		// alleen de objecten die niet vanzelfsprekend een waarde hebben worden hier geinitialiseerd.
		Object city = "0 - No data available";
		Object aqif = "0 - No data available";
		Object no2_value = "0 - No data available";
		Object p_value = "0 - No data available";
		Object o3_value = "0 - No data available";
		Object pm25_value = "0 - No data available";
		Object t_value = "0 - No data available";
		Object so2_value = "0 - No data available";
		Object h_value = "0 - No data available";
		Object pm_value = "0 - No data available";
		Object co_value = "0 - No data available";
		Object wd_value = "0 - No data available";
		Object nameorg = "Unknown organisation";
		Object displaytime = "No time provided";
		Object urlorg	= "Unknown website";
		Object longitude = "52.0612053";
		Object latitude	= "5.1604964";
		
		//Hier wordt de JSON simple library gebruikt.
		// Alleen als de API request succesvol is wordt het JSONObject uitgelezen.
		// Het return JsonObject aanmaken.
		Aqi out = null;
		
		// Status is een check die kijkt of de Request naar Waqi wel gelukt is, als deze request is 
		// gefaald is er geen "ok" of sowieso inhoud in de ontvangen Json String.
		Object status_value = jsonObject.get("status");
		if (status_value.equals("ok")){
            JSONObject data = (JSONObject) jsonObject.get("data");          
            JSONObject iaqi = (JSONObject) data.get("iaqi");
            
            /* hier staan alle objects in volgorde: data -> iaqi -> 'betreffende parameter', deze objecten bevatten afwisselend Longs, doubles etc. waardoor het essentieel is 
            om de waarde in een object op te slaan om class exceptions te verkomen. de waarden worden opgeslagen als volgt: {v: $VALUE}. De if checks zijn om 
            te checken of er geen lege waardes zijn, dit resulteert namelijk in een NullPointerException.*/
            JSONObject no2 = (JSONObject) iaqi.get("no2");
            if (no2 != null){
            	no2_value = no2.get("v");
            }
            JSONObject p = (JSONObject) iaqi.get("p");
            if (p != null){
            	p_value = p.get("v");
            }
            JSONObject o3 = (JSONObject) iaqi.get("o3");
            if (o3 != null){
            	o3_value = o3.get("v");
            }
            JSONObject pm25 = (JSONObject) iaqi.get("pm25");
            if (pm25 != null){
            	pm25_value = pm25.get("v");
            }     
            JSONObject t = (JSONObject) iaqi.get("t");
            if (t != null){	
            	t_value = t.get("v");
            }
            JSONObject so2 = (JSONObject) iaqi.get("so2");
            if (so2 != null){
            	so2_value = so2.get("v");
            }
            JSONObject h = (JSONObject) iaqi.get("h");
            if (h != null){
            	h_value = h.get("v");
            }
            JSONObject pm = (JSONObject) iaqi.get("pm10");
            if (pm != null){
            	pm_value = pm.get("v");
            }
            JSONObject co = (JSONObject) iaqi.get("co");
            if (co != null){
            	co_value = co.get("v");
            }
            JSONObject wd = (JSONObject) iaqi.get("wd");
            if (wd != null){
            	wd_value = wd.get("v");
            }
            aqif = data.get("aqi");
            
            // Hier worden de overige  meegegeven Json Objecten uitgelezen
            JSONObject citydata = (JSONObject) data.get("city");
            	city = citydata.get("name");
	           	JSONObject timedata = (JSONObject) data.get("time");
	           		displaytime = timedata.get("s");
	           	JSONArray attributions = (JSONArray) data.get("attributions");
	           		JSONObject attriarray = (JSONObject) attributions.get(0);      
	           			nameorg = attriarray.get("name");
	           			urlorg = attriarray.get("url");
	           	JSONArray geo = (JSONArray) citydata.get("geo");
	           		longitude = geo.get(0);
	           		latitude = geo.get(1);
	           		
	           		//Hier worden alle gegevens in pojo Aqi opgeslagen en vervolgens gereturned
	           		out = new Aqi((Object) city, (Object) aqif, (Object) no2_value, (Object) p_value, (Object) o3_value, (Object) pm25_value, (Object) t_value, (Object) so2_value, (Object) h_value, (Object) pm_value, (Object) co_value, (Object) wd_value, (Object) nameorg, (Object) displaytime, (Object) urlorg, (Object) longitude, (Object) latitude);
	           		
        }else{
        	System.out.println("Sorry, we couldn't find your search on our servers!");
        }
		return out;
	}
}
