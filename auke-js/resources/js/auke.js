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
			+ "<li>GPS: " + data.lat + "/" + data.lon + "</li>" + "<li>Speed:"
			+ data.speed + "</li> " + "<li>Altitude: " + data.altitude
			+ "</li></ul>"

}

function showInfo(marker, map) {
	var infoWindow = new google.maps.InfoWindow();
	google.maps.event.addListener(marker, 'click', function() {
		$.ajax({
			url : 'service/feed/' + marker.id,
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

function getPointInBoundaryByAPI() {
	$.ajax({
		url : 'service/feed/get-all',
		dataType : 'json',
		contentType : "text/html; charset=utf-8",
		success : function(response) {
			var datas = response.data;
			var center = datas[0];// Just for test;
			var map = initCenter(center.lat, center.lon, 7);
			google.maps.event.addListener(map, 'idle', function(ev) {
				bounds = map.getBounds();
				var result = [];
				note = jQuery('#result');
				for (var j = 1; j < temp.length; j++) {
					latLng = new google.maps.LatLng(temp[j].lat, temp[j].lon);

					if (bounds.contains(latLng)) {
						result[j] = temp[j].id;
					}
				}
				note.empty();
				for (var k = 1; k < result.length; k++) {
					note.append(" ");
					note.append(result[k]);
				}

			});

			var temp = [];
			for (var i = 1; i < datas.length; i++) {
				var positionUnit = datas[i];
				latLng = new google.maps.LatLng(positionUnit.lat,
						positionUnit.lon);
				temp[i] = positionUnit;
				var marker = new google.maps.Marker({
					map : map,
					position : latLng,
					id : positionUnit.id,
					title : positionUnit.id
				});
				showInfo(marker, map);
			}
		}
	});
}

var result = [];// just for test
function init() {
	var latLngCenter = new google.maps.LatLng(10.8230989, 106.6296638);// Uk
	var map = new google.maps.Map(document.getElementById('map-canvas'), {
		'zoom' : 5,
		'center' : latLngCenter,
		'mapTypeId' : google.maps.MapTypeId.ROADMAP,
		'mapTypeControl' : false
	});

	makeRandomDronesOn(map);
	google.maps.event.addListener(map, 'zoom_changed', function(ev) {
		var mapBound = map.getBounds();
		for (var j = 1; j < result.length; j++) {
			latLng = new google.maps.LatLng(result[j].lat, result[j].lon);
			var marker = new google.maps.Marker({
				position : latLng,
				id : result[j].id,
				title : result[j].id
			});
			if (mapBound.contains(latLng)) {
				marker.setMap(map);
				showInfo(marker, map);
			} else {
				marker.setMap(null);
			}
		}
	})

	var interval1 = setInterval(function() {
	}, 10000);
}

function makeRandomDronesOn(map) {
	$.ajax({
		url : 'service/feed/make-drone',
		dataType : 'json',
		contentType : "application/json; charset=utf-8",
		type : 'POST',
		success : function(response) {
			result = response.data;
		}
	});
}

google.maps.event.addDomListener(window, 'load', init);
