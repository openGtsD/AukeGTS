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

function sendRequest() {
	$.ajax({
		url : 'service/feed/get-all',
		dataType : 'json',
		contentType : "text/html; charset=utf-8",
		success : function(response) {
			var datas = response.data;
			var center = datas[0];// Just for test;
			var map = initCenter(center.lat, center.lon, 7);
			var circle = new google.maps.Circle({
				map : map,
				clickable : false,
				// metres
				radius : 100000,
				fillColor : '#fff',
				fillOpacity : .6,
				strokeColor : '#313131',
				strokeOpacity : .4,
				strokeWeight : .8
			});

			latLngCMarker = new google.maps.LatLng(center.lat, center.lon);
			markerCenter = new google.maps.Marker({
				position : latLngCMarker,
				title : 'Ho Chi Minh City Center',
				map : map,
				draggable : true,
				icon : 'http://maps.google.com/mapfiles/ms/icons/green-dot.png'
			});
			circle.bindTo('center', markerCenter, 'position');

			google.maps.event.addListener(markerCenter, 'dragend', function() {
				latLngCenter = new google.maps.LatLng(markerCenter.position
						.lat(), markerCenter.position.lng());

				bounds = circle.getBounds();
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

function init() {
	var mapBound = "";
	var latLngCenter = new google.maps.LatLng(10.8230989, 106.6296638);// Uk
	var map = new google.maps.Map(document.getElementById('map-canvas'), {
		'zoom' : 1,
		'center' : latLngCenter,
		'mapTypeId' : google.maps.MapTypeId.ROADMAP,
		'mapTypeControl' : false
	});

	makeRandomDronesOn(map);

	google.maps.event.addListener(map, 'idle', function(ev) {
		mapBound = map.getBounds();
	});

	var interval1 = setInterval(function() {
		mapBound = map.getBounds();
		var ne = mapBound.getNorthEast(); // LatLng of the north-east corner
		var sw = mapBound.getSouthWest();
		$.ajax({
			url : 'service/feed/load-drone-in-view',
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
				var datas = response.data;
				console.log(">>>>" + datas);
				for (var i = 0; i < datas.length; i++) {
					var positionUnit = datas[i];
					console.log(positionUnit.id);
					latLng = new google.maps.LatLng(positionUnit.lat,
							positionUnit.lon);
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
//		clearInterval(interval1);
	}, 5000);
}

function makeRandomDronesOn(map) {
	$.ajax({
		url : 'service/feed/make-drone',
		dataType : 'json',
		contentType : "application/json; charset=utf-8",
		type : 'POST',
		success : function(response) {
			var datas = response.data;
			for (var i = 0; i < datas.length; i++) {
				var positionUnit = datas[i];
				latLng = new google.maps.LatLng(positionUnit.lat,
						positionUnit.lon);
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

google.maps.event.addDomListener(window, 'load', init);
