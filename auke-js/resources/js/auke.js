function initCenter(lat, lon, zoom) {
	var mapCenter = new google.maps.LatLng(lat, lon);
	var map = new google.maps.Map(document.getElementById('map-canvas'), {
		'zoom' : zoom,
		'center' : mapCenter,
		'mapTypeId' : google.maps.MapTypeId.ROADMAP,
		'mapTypeControl' : false
	});
	return map;
}

function buildHTML(data) {

	return "<h1>Drone Info</h1><ul>" + "<li>Drone ID:" + data.id + "</li>"
			+ "<li>GPS: " + data.currentPosition.latitude + "/"
			+ data.currentPosition.longitude + "</li>" + "<li>Speed:"
			+ data.speed + "</li> " + "<li>Altitude: " + data.altitude
			+ "</li></ul>"

}

function showInfo(marker, map) {
	var infoWindow = new google.maps.InfoWindow();
	google.maps.event.addListener(marker, 'click', function() {
		$.ajax({
			url : 'service/drone/' + marker.id,
			dataType : 'json',
			contentType : "text/html; charset=utf-8",
			success : function(response) {
				var infoWindow = new google.maps.InfoWindow();
				var data = response.data[0];
				var droneInfo = buildHTML(data);
				infoWindow.setContent(droneInfo);
				infoWindow.open(map, marker);
			}
		})
	});
}

var result = [];// just for test
function init() {
	var latLngCenter = new google.maps.LatLng(10.8230989, 106.6296638);// Uk
	var map = new google.maps.Map(document.getElementById('map-canvas'), {
		'zoom' : 0,
		'center' : latLngCenter,
		'mapTypeId' : google.maps.MapTypeId.ROADMAP,
		'mapTypeControl' : false
	});

	makeRandomDronesOn(map);
	var makers = [];
	var interval1 = setInterval(function() {
		var mapBound = map.getBounds();
		for (var j = 1; j < result.length; j++) {
			latLng = new google.maps.LatLng(result[j].currentPosition.latitude,
					result[j].currentPosition.longitude);
			var marker = new google.maps.Marker({
				position : latLng,
				id : result[j].id,
				title : result[j].name
			});
			if (mapBound.contains(latLng)) {
				marker.setMap(map);
				showInfo(marker, map);
				makers.push(marker);
			} else {
				marker.setMap(null);
			}
		}
	}, 5000);
	var counter = 0;
	var interval2 = setInterval(function() {
		counter++;
		var mapBound = map.getBounds();
		for (var j = 1; j < makers.length; j++) {
			if (mapBound.contains(makers[j].getPosition())) {
				makers[j].setMap(null);
				var droneId = makers[j].id;
				$.ajax({
					url : 'service/drone/' + droneId,
					dataType : 'json',
					contentType : "text/html; charset=utf-8",
					success : function(response) {
						var data = response.data[0];
						latLng = new google.maps.LatLng(
								data.currentPosition.latitude,
								data.currentPosition.longitude);
						var marker = new google.maps.Marker({
							position : latLng,
							id : data.id,
							title : data.name
						});
						marker.setMap(map);
					}
				})
			}
		}
		if (counter >= 1000) {
			window.clearInterval(interval2);
		}
	}, 5000)
}

function makeRandomDronesOn(map) {
	$.ajax({
		url : 'service/drone/getall',
		dataType : 'json',
		contentType : "application/json; charset=utf-8",
		type : 'GET',
		success : function(response) {
			result = response.data;
		}
	});
}

google.maps.event.addDomListener(window, 'load', init);
