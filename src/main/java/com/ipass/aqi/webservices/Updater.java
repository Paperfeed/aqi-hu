package com.ipass.aqi.webservices;

import java.io.IOException;

import com.ipass.aqi.DAO.AqiDAOImpl;

public class Updater extends Thread {

	// run wordt gestart zodra deze thread wordt aangemaakt
	public void run() {
		AqiDAOImpl fillDB = new AqiDAOImpl();
		AqiApiRequest req = new AqiApiRequest();

		// Oneindige loop
		while(true){
			// De server is net gestart dus updaten is nog niet nodig, Updater begint met een pauze van 30 minuten
			try {
				Thread.sleep(1800000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Ontwaak en update de Database gegevens met de nieuwe API gegevens
			System.out.println("Updating Database...");
			try {
				fillDB.update(req.doGet("Paris"));
				fillDB.update(req.doGet("Berlin"));
				fillDB.update(req.doGet("New York"));
				System.out.println("Update successful!");
				System.out.println("Thread going to sleep...");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
