package com.ipass.aqi.webservices;

import java.io.*;
import java.util.List;
import com.google.gson.Gson;
import com.ipass.aqi.DAO.Aqi;
import com.ipass.aqi.DAO.AqiDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
public class AqiResource {

	// Jersey path, een GET request naar de @Path wordt naar deze functie
	// doorverwezen
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("aqi/{req}")
	public Response doGet(@PathParam("req") String req) throws IOException {
		AqiApiRequest apireq = new AqiApiRequest();
		Aqi jsonresult = apireq.doGet(req);

		// Na het importen en uitlezen van het JSON object in van de API
		// AqiApiRequest zijn we nu bij het punt waar we zelf een JSON
		// Object maken, deze kunnen we vervolgens met een GET vanuit javascript
		// opvragen.
		// BELANGRIJK!! Er wordt hiet niet gebruik gemaakt van json simple maar
		// van Gson, een json library van Google.
		AqiDAO ref = new AqiDAO();
		List<Aqi> all = ref.findAll();

		Gson gson = new Gson();

		// Hier wordt een nieuw JSON object aangemaakt met zowel de data van de
		// search als van de Database erin
		// het resultaat is dusL
		// positie 0: wat de gebruiker zoekt
		// positie 1: gegevens uit de database
		// positie 2: gegevens uit de database
		// positie 3: gegevens uit de database
		all.add(0, jsonresult);
		// Het bouwen van de response, de response wordt met gson naar json veranderd.
		return Response.accepted().entity(gson.toJson(all)).build();
	}
}
