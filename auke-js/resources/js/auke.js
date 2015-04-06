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

var markers = [];// just for test
function loadDroneIncurrentView(map) {
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
			$("#numberDrone").text(data.length);
			clearMarkers();
			for (var i = 0; i < data.length; i++) {
				latLng = new google.maps.LatLng(
						data[i].currentPosition.latitude,
						data[i].currentPosition.longitude);
				var marker = new google.maps.Marker({
					position : latLng,
					id : data[i].id,
					title : data[i].name,
					icon : '/auke-js/resources/images/drone.png'

				});
				marker.setMap(map);
				showInfo(marker, map);
				markers.push(marker);

			}

		}
	});
}

function moveDroneIncurrentView(map) {
	var mapBound = map.getBounds();
	for (var i = 1; i < markers.length; i++) {
		var oldMarker = markers[i];
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
					} else {
						oldMarker.setMap(null);
					}
				}
			})
		}
	}
}

function init() {
	var latLngCenter = new google.maps.LatLng(59.913869, 10.752245);// OSLO
	var map = new google.maps.Map(document.getElementById('map-canvas'), {
		'zoom' : 3,
		'center' : latLngCenter

	});

	google.maps.event.addListener(map, 'idle', function() {
		var mapBound = map.getBounds();
		$("#resultZoom").text(map.getZoom());
		$('#resultCenter').text(
				map.getCenter().lat() + "/" + map.getCenter().lng());
		var ne = mapBound.getNorthEast(); // LatLng of the north-east corner
		var sw = mapBound.getSouthWest();
		var southWestLat = "Upper Left: " + sw.lat() + " / " + sw.lng();
		var northEastLat = "Lower Right: " + ne.lat() + " / " + ne.lng();
		$("#resultBoundary").html(southWestLat + "</br>" + northEastLat);
		loadDroneIncurrentView(map);
	});
//
//	 var interval2 = setInterval(function() {
//		moveDroneIncurrentView(map);
//	}, 5000)
}

function setAllMap(map) {
	for (var i = 0; i < markers.length; i++) {
		markers[i].setMap(map);
	}
}

// Removes the markers from the map, but keeps them in the array.
function clearMarkers() {
	setAllMap(null);
}

google.maps.event.addDomListener(window, 'load', init);
