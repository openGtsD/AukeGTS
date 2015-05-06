Ext.ns('Auke.utils');
// Change url when go live
Auke.utils.baseURL = "http://localhost:8080/";
Auke.utils.serviceURL = "http://localhost:8888/";

Auke.utils.buildURL = function (url, isUseService) {
    var domain = isUseService ? Auke.utils.serviceURL : Auke.utils.baseURL;
    return domain + url;
};


Auke.utils.loadViewFromHash = function(hashString) {
	if (!hashString) {
		return;
	}

	var hashArr = hashString.split(':');
	var viewName = hashArr[0];
	var viewParams = hashArr.slice(1);

	var viewContainer = Ext.getCmp('viewContainer');
	var view = Ext.create('Auke.view.' + viewName, {
		params : viewParams
	});
	if (viewContainer) {
		viewContainer.removeAll(true);
		viewContainer.add(view);
	}
};

Auke.utils.loadView = function(viewName, viewParams) {
	if (!viewName) {
		return null;
	}
	oldToken = Ext.History.getToken();
	newToken = viewName;
	if (viewParams && viewParams.length) {
		newToken += ':' + viewParams.join(':');
	}
	if (oldToken != newToken) {
		Ext.History.add(newToken);
	}
};

Auke.utils.buildHTML = function(data) {
	return "<h1>Position Info</h1><ul>" + "<li>Drone ID:" + data.id
			+ "</li><li>GPS: " + data.currentPosition.latitude + "/"
			+ data.currentPosition.longitude + "</li><li>numtrackers:"
			+ data.numtrackers
	"</li></ul>"

}

var map;
var mgr;
var icons = {};
Auke.utils.markers = [];
Auke.utils.allmarkers = {};
var layerId = "SIMULATED";
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
			updateStatus(mgr.getMarkerCount(map.getZoom()));
		});

		var interval = setInterval(function() {
			loadDroneIncurrentView(layerId);
		}, 5000);
	});
}

Auke.utils.createMarker = function(id, posn, title, contentHTML, map) {
	var markerOptions = {
		id : id,
		position : posn,
		title : title,
		content : contentHTML
	};
	var marker = new google.maps.Marker(markerOptions);
	Auke.utils.createInfoWindow(marker, map);
//	Auke.utils.centerZoom(marker, map);
	return marker;
}

var infoWindow = new google.maps.InfoWindow();
Auke.utils.createInfoWindow = function(marker, map) {
	google.maps.event.addListener(marker, 'click', function() {
		infoWindow.setContent(marker.content);
		infoWindow.open(map, marker);
	});
}

Auke.utils.centerZoom = function(marker, map) {
	google.maps.event.addListener(marker, 'dblclick', function() {
		var bounds = new google.maps.LatLngBounds();
		bounds.extend(marker.position);
		map.fitBounds(bounds);
		map.setZoom(16);
	});
}

Auke.utils.stop = function(id) {
	Ext.Ajax.request({
		url : Auke.utils.buildURL('drone/stop/', true) + id,
		success : function(response) {
			var res = Ext.JSON.decode(response.responseText);
			var data = res[0];
			Auke.utils.changeMarkerPosition(data);
		}
	})
}

Auke.utils.start = function(id) {
	Ext.Ajax.request({
		url : Auke.utils.buildURL('drone/start/', true) + id,
		success : function(response) {
			var res = Ext.JSON.decode(response.responseText);
			var data = res[0];
			Auke.utils.changeMarkerPosition(data);
		}
	})
}

Auke.utils.changeMarkerPosition = function(data) {
	var newPosition = new google.maps.LatLng(data.currentPosition.latitude,
			data.currentPosition.longitude);
	var marker = Auke.utils.allmarkers[data.id];
	marker.setPosition(newPosition);
}

function autoCenter(markers) {
	var bounds = new google.maps.LatLngBounds();
	$.each(markers, function(index, marker) {
		bounds.extend(marker.position);
	});
	map.fitBounds(bounds);
}
