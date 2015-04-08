function buildHTML(data) {

	return "<h1>Drone Info</h1> <input type='button' onclick=start("
			+ "'" + data.id + "'" + ") value='Start Moving'  /> <ul>"
			+ "<li>Drone ID:" + data.id + "</li>" + "<li>GPS: "
			+ data.currentPosition.latitude + "/"
			+ data.currentPosition.longitude + "</li>" + "<li>Speed:"
			+ data.speed + "</li> " + "<li>Altitude: " + data.altitude
			+ "</li></ul>"

}

var map;
var mgr;
var icons = {};
var allmarkers = {};
function load() {
	var myOptions = {
		zoom : 3,
		center : new google.maps.LatLng(50.62504, -100.10742),
		mapTypeId : google.maps.MapTypeId.ROADMAP
	}
	map = new google.maps.Map(document.getElementById('map-canvas'), myOptions);

	mgr = new MarkerManager(map);

	google.maps.event.addListener(mgr, 'loaded', function() {
		google.maps.event.addListener(map, 'idle', function() {
			$("#resultZoom").text(map.getZoom());
			var mapBound = map.getBounds();
			var ne = mapBound.getNorthEast();
			var sw = mapBound.getSouthWest();
			var southWestLat = "Upper Left: " + sw.lat() + " / " + sw.lng();
			var northEastLat = "Lower Right: " + ne.lat() + " / " + ne.lng();
			$("#resultBoundary").html(southWestLat + " And " + northEastLat);
			loadDroneIncurrentView();
			updateStatus(mgr.getMarkerCount(map.getZoom()));
		});
	});
}

function loadDroneIncurrentView() {
	allmarkers.length = 0;
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
			mgr.clearMarkers();
			var markers = [];
			for (var i = 0; i < data.length; i++) {
				posn = new google.maps.LatLng(data[i].currentPosition.latitude,
						data[i].currentPosition.longitude);
				var marker = createMarker(data[i].id, posn, data[i].name,
						'/auke-js/resources/images/drone.png');
				markers.push(marker);
				allmarkers[data[i].id] = marker;
			}
			// mgr.addMarkers(markers, data[i].minZoom, data[i].maxZoom); TODO:
			// its use full for show drone in zoom factory
			mgr.addMarkers(markers, 3, 9);
			mgr.refresh();
			updateStatus(mgr.getMarkerCount(map.getZoom()));
		}
	});
}

function createMarker(id, posn, title, icon) {
	var markerOptions = {
		id : id,
		position : posn,
		title : title,
		icon : icon
	};
	// if (icon !== false) {
	// markerOptions.shadow = icon.shadow;
	// markerOptions.icon = icon.icon;
	// markerOptions.shape = icon.shape;
	// }

	var marker = new google.maps.Marker(markerOptions);
	google.maps.event.addListener(marker, 'click', function() {
		var infoWindow = new google.maps.InfoWindow();
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
	return marker;
}

function updateStatus(html) {
	document.getElementById("numberDrone").innerHTML = html;
}

// ---- Test button
function reloadMarkers() {
	loadDroneIncurrentView();
}

function start(id) {
	counter = 0;
	var speed = 100; // just for test
	var interval1 = setInterval(function() {
		$.ajax({
			url : 'service/drone/move/' + id + "/" + speed,
			dataType : 'json',
			contentType : "text/html; charset=utf-8",
			success : function(response) {
				var data = response.data[0];
				changeMarkerPosition(data);
			}
		})
		if(counter == speed*2) {
			clearInterval(interval1);
		}	
		counter++;
	}, 20);

}

function changeMarkerPosition(data) {
	var newPosition = new google.maps.LatLng(data.currentPosition.latitude,
			data.currentPosition.longitude);
	var marker = allmarkers[data.id];
	marker.setPosition(newPosition);
}

google.maps.event.addDomListener(window, 'load', load);
