// Hier de-bind ik het dollar teken zodat er geen conflict met canvasjs ontstaat
$.noConflict();
// Dit zijn de 2 variablen die de locatie van de google maps widget bepalen
var googlelat = 52.0612053;
var googlelng = 5.1604964;

// Hiermee wordt de googlemaps widget (asynchroon) geinitialiseerd 
function initMap() {
	var map = new google.maps.Map(document.getElementById('map'), {
		center : new google.maps.LatLng(googlelat, googlelng),
		mapTypeId : google.maps.MapTypeId.ROADMAP,
		zoom : 11
	});

	var t = new Date().getTime();
	var waqiMapOverlay = new google.maps.ImageMapType(
			{
				getTileUrl : function(coord, zoom) {
					return 'https://tiles.waqi.info/tiles/usepa-aqi/'+ zoom + "/" + coord.x + "/" + coord.y + ".png?token=3817177d29c5845192f55c3a2a4ca92fe16e93ee";
				},
				name : "Air  Quality",
			});
	map.overlayMapTypes.insertAt(0, waqiMapOverlay);
}

// Ready, oftewel pas laden als het html document geladen is
jQuery(document).ready(function() {
	
	
// De URL waar requests naartoe moeten worden gestuurd
var rootURL = "/aqi/rest/aqi";

	// Bij het drukken van enter in de zoekbalk wordt deze functie uitgevoerd
	jQuery("#press-enter").keydown(function(ev){
    if (ev.which === 13){
    	findAll();
    	}   
    });
	
	
	// Deze functie haalt alle 4 de API requests op
	function findAll() {
		// Get json in plaats van .ajax, getjson is simpel en netjes, de url wordt gemaakt van de root+ de invoer in de zoekbalk
		jQuery.getJSON(rootURL+"/"+jQuery('#press-enter').val(), function(res){
			
			// Een simpele check 
			if(res[0]["city"] != null){
			
			// Omdat in de backend de zoekopdracht altijd wordt geplaatst op positie 1 is data0 het (resultaat) object van de zoekopdracht
			var data0 = res[0];
			var data1 = res[1];
			var data2 = res[2];
			var data3 = res[3];
			
			
			//Het uitlezen van het object, dit heb ik dynamisch ($each) geprobeerd maar aangezien het toewijzen van variablen 
			// niet dynamisch kan heb ik gekozen om dit "handmatig" te doen. Wellicht kon het met window maar vanwege 
			// de relatief kleine hoeveelheid data heb ik voor deze oplossing gekozen
			var city0 = data0["city"];
			var aqif0 = data0["aqif"];
			var no20 = data0["no2"];
			var p0 = data0["p"];
			var o30 = data0["o3"];
			var pm250 = data0["pm25"];
			var t0 = data0["t"];
			var so20 = data0["so2"];
			var h0 = data0["h"];
			var pm0 = data0["pm"];
			var co0 = data0["co"];
			var wd0 = data0["wd"];
			var nameorg1 = data0["nameorg"];
			var displaytime0 = data0["displaytime"];
			var urlorg0 = data0["urlorg"];
			var longitude0 = data0["longitude"];
			var latitude0 = data0["latitude"];
			
			// Hier worden de googlemaps coordinaten aangepast naar de gezochte plek en de map opnieuw ingesteld
			googlelat = latitude0;
			googlelng = longitude0;
			initMap();
			
			
			// Hier worden de gegevens in de html pagina geplaatst op de respectievelijke plek 
			jQuery("#city").html(city0);
			jQuery("#aqif").html(aqif0);
			jQuery("#no2").html(no20);
			jQuery("#p").html(p0);
			jQuery("#o3").html(o30);
			jQuery("#pm25").html(pm250);
			jQuery("#t").html(t0);
			jQuery("#so2").html(so20);
			jQuery("#h").html(h0);
			jQuery("#pm").html(pm0);
			jQuery("#co").html(co0);
			jQuery("#wd").html(wd0);
			jQuery("#nameorg").html(nameorg1);
			jQuery("#displaytime").html(displaytime0);
			jQuery("#urlorg").html(urlorg0);
			jQuery("#longitude").html(longitude0);
			jQuery("#latitude").html(latitude0);
			
			
			// Herhaling van het uitlezen
			var city1 = data1["city"];
			var aqif1 = data1["aqif"];
			var no21 = data1["no2"];
			var p1 = data1["p"];
			var o31 = data1["o3"];
			var pm251 = data1["pm25"];
			var t1 = data1["t"];
			var so21 = data1["so2"];
			var h1 = data1["h"];
			var pm1 = data1["pm"];
			var co1 = data1["co"];
			var wd1 = data1["wd"];
			var nameorg1 = data1["nameorg"];
			var displaytime1 = data1["displaytime"];
			var urlorg1 = data1["urlorg"];
			var longitude1 = data1["longitude"];
			var latitude1 = data1["latitude"];			
			
			// Herhaling van het uitlezen
			var city2 = data2["city"];
			var aqif2 = data2["aqif"];
			var no22 = data2["no2"];
			var p2 = data2["p"];
			var o32 = data2["o3"];
			var pm252 = data2["pm25"];
			var t2 = data2["t"];
			var so22 = data2["so2"];
			var h2 = data2["h"];
			var pm2 = data2["pm"];
			var co2 = data2["co"];
			var wd2 = data2["wd"];
			var nameorg2 = data2["nameorg"];
			var displaytime2 = data2["displaytime"];
			var urlorg2 = data2["urlorg"];
			var longitude2 = data2["longitude"];
			var latitude2 = data2["latitude"];	
			
			// Herhaling van het uitlezen
			var city3 = data3["city"];
			var aqif3 = data3["aqif"];
			var no23 = data3["no2"];
			var p3 = data3["p"];
			var o33 = data3["o3"];
			var pm253 = data3["pm25"];
			var t3 = data3["t"];
			var so23 = data3["so2"];
			var h3 = data3["h"];
			var pm3 = data3["pm"];
			var co3 = data3["co"];
			var wd3 = data3["wd"];
			var nameorg3 = data3["nameorg"];
			var displaytime3 = data3["displaytime"];
			var urlorg3 = data3["urlorg"];
			var longitude3 = data3["longitude"];
			var latitude3 = data3["latitude"];			
			
			// Een simpele notificatie voor als de gezochte locatie geen resultaat oplevert
				}else{
					alert("The location you've requested either doesn't exist or isn't spelled correctly, please try again.");
				}
			
			// Hier wordt de eerste chart aangemaakt (de chart waar de gezochte plaat komt te staan)
			var city0chart = new CanvasJS.Chart("chartContainer0", {
					theme: "theme2",//theme1
					// De titel van de grafiek
					title: {
						text: "Column Chart: "+city0
					},
			                animationEnabled: true,
			        //data in de chart, array met objecten
					data: [
					{
						type: "column", //change it to line, area, bar, pie, etc
						dataPoints: [
							{ label: "NO2", y: no20 },
							{ label: "O3", y: o30 },
							{ label: "PM2.5", y: pm250 },
							{ label: "PM10", y: pm0 },
							{ label: "SO2", y: so20 },
							{ label: "CO", y: co0 },
							{ label: "Humidity", y: h0 },
							{ label: "Pressure (x100)", y: p0/100 },
							{ label: "Temperature", y: t0 }
						]
					}
					]
				});
				// Dit zorgt ervoor dat de chart wordt gerenderd
				city0chart.render();
				
			// de tweede chart
			var city1chart = new CanvasJS.Chart("chartContainer1", {
					theme: "theme2",//theme1
					title: {
						text: "Column Chart: "+city1
					},
			                animationEnabled: true,
					data: [
					{
						type: "column", //change it to line, area, bar, pie, etc
						dataPoints: [
							{ label: "NO2", y: parseInt(no21) },
							{ label: "O3", y: parseInt(o31) },
							{ label: "PM2.5", y: parseInt(pm251) },
							{ label: "PM10", y: parseInt(pm1) },
							{ label: "SO2", y: parseInt(so21) },
							{ label: "CO", y: parseInt(co1) },
							{ label: "Humidity", y: parseInt(h1) },
							{ label: "Pressure (x100)", y: p1/100 },
							{ label: "Temperature", y: parseInt(t1) }
						]
					}
					]
				});
				city1chart.render();
				
			// de derde chart
			var city2chart = new CanvasJS.Chart("chartContainer2", {
					theme: "theme2",//theme1
					title: {
						text: "Column Chart: "+city2
					},
			                animationEnabled: true,
					data: [
					{
						type: "column", //change it to line, area, bar, pie, etc
						dataPoints: [
							{ label: "NO2", y: parseInt(no22) },
							{ label: "O3", y: parseInt(o32) },
							{ label: "PM2.5", y: parseInt(pm252) },
							{ label: "PM10", y: parseInt(pm2) },
							{ label: "SO2", y: parseInt(so22) },
							{ label: "CO", y: parseInt(co2) },
							{ label: "Humidity", y: parseInt(h2) },
							{ label: "Pressure (x100)", y: p2/100 },
							{ label: "Temperature", y: parseInt(t2) }
						]
					}
					]
				});
				city2chart.render();
				
			// de vierde chart
			var city3chart = new CanvasJS.Chart("chartContainer3", {
					theme: "theme2",//theme1
					title: {
						text: "Column Chart: "+city3
					},
			                animationEnabled: true,
					data: [
					{
						type: "column", //change it to line, area, bar, pie, etc
						dataPoints: [
							{ label: "NO2", y: parseInt(no23) },
							{ label: "O3", y: parseInt(o33) },
							{ label: "PM2.5", y: parseInt(pm253) },
							{ label: "PM10", y: parseInt(pm3) },
							{ label: "SO2", y: parseInt(so23) },
							{ label: "CO", y: parseInt(co3) },
							{ label: "Humidity", y: parseInt(h3) },
							{ label: "Pressure (x100)", y: p3/100 },
							{ label: "Temperature", y: parseInt(t3) }
						]
					}
					]
				});
				city3chart.render();
			// END findAll()
			});
		}
// Sluiting van "document.ready"
});

