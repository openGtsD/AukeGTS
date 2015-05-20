Ext.ns('Auke.utils');
Auke.utils.mgr = "";
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
var icons = {};
Auke.utils.markers = [];
Auke.utils.allmarkers = {};
var layerId = "SIMULATED";

Auke.utils.createMarker = function(id, posn, title) {
	var markerOptions = {
		id : id,
		position : posn,
		title : title
	};
	var marker = new google.maps.Marker(markerOptions);
	Auke.utils.createInfoWindow(marker, map);
//	Auke.utils.centerZoom(marker, map);
	return marker;
}

Auke.utils.createInfoWindow = function(marker, map) {
	google.maps.event.addListener(marker, 'click', function() {
		Auke.utils.getIncludedTracker(map, marker)
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

var infoWindow = new google.maps.InfoWindow();
Auke.utils.getIncludedTracker = function(map, marker) {
	Ext.Ajax.request({
		url : Auke.utils.buildURL('drone/get-tracker/', true) + marker.id,
		method: 'GET',
		success : function(response) {
			var res = Ext.JSON.decode(response.responseText);
			if (res.success) {
				infoWindow.setContent(Auke.utils.buildHTML(res[0]));
				infoWindow.open(map, marker);
			}
		}
	})
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
