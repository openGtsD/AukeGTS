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
	var latLngCenter = new google.maps.LatLng(10.8230989, 106.6296638);
	var map = new google.maps.Map(document.getElementById('map-canvas'), {
		'zoom' : 3,
		'center' : latLngCenter,
		'mapTypeId' : google.maps.MapTypeId.ROADMAP,
		'mapTypeControl' : false
	});
	var markers = [];
	var interval1 = setInterval(function() {
		var mapBound = map.getBounds();
		var ne = mapBound.getNorthEast(); // LatLng of the north-east corner
		var sw = mapBound.getSouthWest();
		$.ajax({
			url : 'service/drone/load-drone-in-view',
			dataType : 'json',
			contentType : "application/json; charset=utf-8",
			data : JSON.stringify({
				southWestLat : sw.lat(),
				southWestLon : sw.lng(),
				northEastLat : ne.lat(),
				northEastLon : ne.lng()
			}),
			type : 'POST',
			success : function(response) {
				var data = response.data;
				for (var i = 0; i < data.length; i++) {
					latLng = new google.maps.LatLng(
							data[i].currentPosition.latitude,
							data[i].currentPosition.longitude);
					var marker = new google.maps.Marker({
						position : latLng,
						id : data[i].id,
						title : data[i].name,
						map: map
					});
					showInfo(marker, map);
					markers.push(marker);
				}
			}
		});
	}, 10000);
	var counter = 0;
	var interval2 = setInterval(function() {
		counter++;
		var mapBound = map.getBounds();
		for (var i = 1; i < markers.length; i++) {
			var oldMarker = markers[i];
			oldMarker.setMap(null);
			if (mapBound.contains(oldMarker.getPosition())) {
				var droneId = oldMarker.id;
				$.ajax({
					url : 'service/drone/' + droneId,
					dataType : 'json',
					contentType : "text/html; charset=utf-8",
					success : function(response) {
						var data = response.data[0];
						if (data) {
							latLng = new google.maps.LatLng(
									data.currentPosition.latitude,
									data.currentPosition.longitude);
							oldMarker.setPosition(latLng);
						}
					}
				})
			} else {
				oldMarker.setMap(null);
			}
		}
	}, 10000)
}

google.maps.event.addDomListener(window, 'load', init);
