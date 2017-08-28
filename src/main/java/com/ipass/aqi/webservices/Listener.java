package com.ipass.aqi.webservices;

import com.google.gson.Gson;
import com.ipass.aqi.DAO.AQIData;
import com.ipass.aqi.DAO.AqiDAOImpl;
import com.ipass.aqi.webservices.Updater;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

public class Listener implements ServletContextListener {
    // Functie die wordt uitgevoerd bij starten server
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("Initializing Database...");
        AqiDAOImpl aqiDAO = new AqiDAOImpl();

        // Hier kan worden gekozen om de table te wipen voordat de server word gestart
        aqiDAO.deleteTable();

        aqiDAO.initDAO();
        // Hier wordt Updater gestart, een thread die om de dertig minuten de database update
        Updater updater = new Updater();
        updater.start();

        System.out.println("Update listener started.");
        System.out.println("Initialisation complete!");
    }

    // Functie die wordt uitgevoerd bij sluiten server
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.out.println("Server shutting down... Good night");
    }

    @Path("/")
    public class AqiResource {
        // Jersey path, een GET request naar de @Path wordt naar deze functie doorverwezen
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        @Path("aqi/{req}")
        public Response doGet(@PathParam("req") String req) throws IOException {
            // In deze method maken we JSon van ons eigen object en sturen dit door
            // AqiApiRequest zijn we nu bij het punt waar we zelf een JSON
            AqiDAOImpl aqidao = new AqiDAOImpl();


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
            System.out.println("RESULT JSon = " + gson.toJson(result));
            return Response.accepted().entity(gson.toJson(result)).build();
        }
    }
}
