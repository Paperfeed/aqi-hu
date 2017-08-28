package com.ipass.aqi.webservices;

import java.io.*;
import java.util.List;
import com.google.gson.Gson;
import com.ipass.aqi.DAO.AQIData;
import com.ipass.aqi.DAO.DAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class AqiResource {
	// Jersey path, een GET request naar de @Path wordt naar deze functie doorverwezen
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("aqi/{req}")
	public Response doGet(@PathParam("req") String req) throws IOException {
		// In deze method maken we JSon van ons eigen object en sturen dit door
		// AqiApiRequest zijn we nu bij het punt waar we zelf een JSON
		// TODO houd DAO connectie open zodat requests niet zo lang duren?
		DAO aqidao = new DAO();

		// Check of de zoekterm al in de database bestaat - zo niet, update
		List<AQIData> result = aqidao.selectAqi("SELECT " + req + " FROM aqi;");
		if (result.isEmpty()) {
			// zoekterm is nog niet in DB, haal op van WAQI
			AqiApiRequest aqiApiRequest = new AqiApiRequest();
			result.add(aqiApiRequest.doGet(req));
		}

		// Haal hier extra info over andere steden op en voeg die toe bij de lijst die uiteindelijk word omgezet
		List<AQIData> extradata = aqidao.selectAqi("SELECT Paris, Shanghai, New York FROM aqi;");
		result.addAll(extradata);

		Gson gson = new Gson();

		// Het bouwen van de response, het object wordt met gson naar json veranderd.
		if (Debug.ON) {	System.out.println("RESULT JSon = " + gson.toJson(result)); }
		return Response.accepted().entity(gson.toJson(result)).build();
	}
}
