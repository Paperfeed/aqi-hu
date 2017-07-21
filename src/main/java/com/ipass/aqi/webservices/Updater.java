package com.ipass.aqi.webservices;

import java.io.IOException;

import com.ipass.aqi.DAO.AqiDAO;

public class Updater extends Thread{
	// run wordt gestart zodra deze thread wordt aangemaakt
	public void run(){
		AqiDAO fillDB = new AqiDAO();
		AqiApiRequest req = new AqiApiRequest();
		// oneindige loop
		while(true){
			// De server is net gestart dus updaten is nog niet nodig, Updater begint met een pauze van 30 minuten
			try {
				Thread.sleep(1800000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// De 30 minuten zijn voorbij en Updater update de Database gegevens met de nieuw API gegevens
			System.out.println("Updating Database...");
			try {
				fillDB.update(req.doGet("Paris"));
				fillDB.update(req.doGet("Berlin"));
				fillDB.update(req.doGet("New York"));
				System.out.println("Update succesful!");
				System.out.println("Thread going to sleep...");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
