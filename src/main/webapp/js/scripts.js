// Hiermee wordt de googlemaps widget (asynchroon) geinitialiseerd
function initMap() {
    if ("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(
        	function(position) {
                updateMap(position.coords.latitude, position.coords.longitude);
            }
		);
    } else {
    	updateMap(52.0612053, 5.1604964);
	}
}

function updateMap(lat, lng) {
	if (lat === null || lng === null){
		lat = 52.0612053;
		lng = 5.1604964;
	}
	
	var map = new google.maps.Map(document.getElementById('map'), {
		center : new google.maps.LatLng(lat, lng),
		mapTypeId : google.maps.MapTypeId.ROADMAP,
		zoom : 11
	});

	var t = new Date().getTime();
	var waqiMapOverlay = new google.maps.ImageMapType(
			{
				getTileUrl : function(coord, zoom) {
					return 'https://tiles.waqi.info/tiles/usepa-aqi/'+ zoom + "/" + coord.x + "/" + coord.y + ".png?token=3817177d29c5845192f55c3a2a4ca92fe16e93ee";
				},
				name : "Air Quality"
			});
	map.overlayMapTypes.insertAt(0, waqiMapOverlay);
}

$(document).ready(function() {
	// De URL waar requests naartoe moeten worden gestuurd
	var rootURL = "/aqi/rest/aqi";

	// Bij het drukken van enter in de zoekbalk wordt deze functie uitgevoerd
    var $searchInput = $("#press-enter");
    $searchInput.keydown(function(ev){
    if ((ev.which === 13) && ($searchInput.val() !== "")) {
    	findAll();
	}
    });
	
	// Deze functie haalt alle 4 de API requests op
	function findAll() {
		// Get json in plaats van .ajax, getjson is simpel en netjes, de url wordt gemaakt van de root+ de invoer in de zoekbalk
		$.getJSON(rootURL + "/" + $searchInput.val(), function(result){

			// Als je plaatsnaam niet in de database bestaat en/of gevonden kan worden
			// laat dan weten aan gebruiker
			if (result[0] === null) {
                //$("#press-enter").attr("placeholder", "Location not found!");
                $searchInput.addClass("animated shake error");
                $searchInput.one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function() {
                    $searchInput.removeClass("animated shake error");
                });
			} else {
                // Eerste iteration is de zoekopdracht, laatste drie zijn reference steden
                $.each(result, function (i, element) {
                    // Vul zoek opdracht informatie in op resultaten formulier
                    if (i === 0) {
                        $("#city").html(element.city);
                        $("#aqif").html(element.aqif);
                        $("#no2").html(element.no2);
                        $("#p").html(element.p);
                        $("#o3").html(element.o3);
                        $("#pm25").html(element.pm25);
                        $("#t").html(element.t);
                        $("#so2").html(element.so2);
                        $("#h").html(element.h);
                        $("#pm").html(element.pm);
                        $("#co").html(element.co);
                        $("#wd").html(element.wd);
                        $("#nameorg").html(element.nameorg);
                        $("#displaytime").html(element.displaytime);
                        $("#urlorg").html(element.urlorg);
                        $("#longitude").html(element.longitude);
                        $("#latitude").html(element.latitude);

                        // Update de map met de nieuwe coordinaten van de zoekopdracht
                        updateMap(element.latitude, element.longitude);
                    }

                    // Creeer charts voor de gekozen locatie + 3 referentie steden
                    $("#chartContainer" + i).CanvasJSChart({
                        theme: "theme2",
                        title: {
                            text: element.city
                        },
                        animationEnabled: true,
                        creditHref: "",
                        creditText: "",
                        data: [
                            {
                                type: "column", // line, area, bar, pie
                                dataPoints: [
                                    { label: "NO2", y: element.no2 },
                                    { label: "O3", y: element.o3 },
                                    { label: "PM2.5", y: element.pm25 },
                                    { label: "PM10", y: element.pm },
                                    { label: "SO2", y: element.so2 },
                                    { label: "CO", y: element.co },
                                    { label: "Humidity", y: element.h },
                                    { label: "Pressure (x100)", y: element.p / 100 },
                                    { label: "Temperature", y: element.t }
                                ]
                            }
                        ]
                    });
				// Einde $each loop
                });
            }
		});
	}
// Sluiting van "document.ready"
});

