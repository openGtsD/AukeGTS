Ext.ns('Auke.utils');
Auke.utils.mgr = "";
Auke.utils.markers = [];
Auke.utils.allmarkers = {};
// Change url when go live
Auke.utils.baseURL = "http://localhost:8080/";
Auke.utils.serviceURL = "http:///localhost:8888/";

Auke.utils.buildURL = function(url, isUseService) {
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

Auke.utils.buildHTML = function(data, isSummarized) {
	if (isSummarized) {
		var html = "<h1>Position Information - Number tracker is: "
				+ data.length + "</h1><ul>"
		for (var i = 0; i < data.length; i++) {
			html += "<li>Tracker ID: <span class='my-link'>" + data[i]
					+ "</span></li>"
		}
		html += "</ul>";
		return html;
	} else {
		var div = document.createElement('DIV');
		div.innerHTML = Auke.utils.buildContent(data[0]);
		return div;
	}
};

Auke.utils.buildContent = function(data) {
	return "<ul>" + "<li>Id: <span class='highlight'>" + data.id
			+ "</span></li><li>GPS: <span class='highlight'>"
			+ data.currentPosition.latitude + " / "
			+ data.currentPosition.longitude
			+ "</span></li><li>Altitude: <span class='highlight'>"
			+ data.currentPosition.altitude
			+ "</span></li><li>Speed: <span class='highlight'>"
			+ data.currentPosition.speed + "</span></li>"
			+ "<li>Name: <span class='highlight'>"
			+ data.name + "</span></li><li>Time: <span class='highlight'>" + new Date(data.currentPosition.time*1000) + "</li></ul>"
};

Auke.utils.createMarker = function(id, posn, title, numtrackers, layerId, map) {
	var ico = map.getZoom() >= 11 ? Auke.utils.baseURL + "auke-js/ui/images/flight.gif" : "";
	var markerOptions = {
		id : id,
		position : posn,
		title : title + " Number Tracker: " + numtrackers,
		icon: ico
	};
	var marker = new google.maps.Marker(markerOptions);
	Auke.utils.createInfoWindow(marker, map, layerId);
	Auke.utils.centerZoom(marker, map);
	return marker;
}

Auke.utils.createInfoWindow = function(marker, map, layerId) {
	google.maps.event.addListener(marker, 'click', function() {
		if (map.getZoom() >= 11) {
			Auke.utils.getTracker(marker, map);
		} else {
			Auke.utils.getIncludedTracker(map, marker, layerId);
		}
	});
}

Auke.utils.centerZoom = function(marker, map) {
	google.maps.event.addListener(marker, 'dblclick', function() {
		var bounds = new google.maps.LatLngBounds();
		bounds.extend(marker.position);
		map.fitBounds(bounds);
		map.setZoom(11);
	});
}

var infoWindow = new google.maps.InfoWindow();
Auke.utils.getIncludedTracker = function(map, marker, layerId) {
	Ext.Ajax.request({
		url : Auke.utils.buildURL('drone/get-included-tracker/', true)
				+ marker.id + "/" + layerId + "/" + map.getZoom(),
		method : 'GET',
		success : function(response) {
			var res = Ext.JSON.decode(response.responseText);
			if (res.success) {
				infoWindow.setContent(Auke.utils.buildHTML(res.data, true));
				infoWindow.open(map, marker)
			}
		}
	})
}

Auke.utils.buildInfoBubble = function() {
	var infoBubble = new InfoBubble({
		shadowStyle : 1,
		padding : 10,
		borderRadius : 5,
		minWidth : 200,
		borderWidth : 1,
		disableAutoPan : true,
		hideCloseButton : false
	});

	return infoBubble;
}

Auke.utils.getTracker = function(marker, map) {
	Ext.Ajax.request({
		url : Auke.utils.buildURL('drone/get-tracker/', true) + marker.id,
		method : 'POST',
		success : function(response) {
			var res = Ext.JSON.decode(response.responseText);
			if (res.success) {
				var content = Auke.utils.buildHTML(res.data, false)
				var posn = new google.maps.LatLng(res.data[0].currentPosition.latitude,
						res.data[0].currentPosition.longitude);
				var infoBubble = Auke.utils.buildInfoBubble();
				infoBubble.close();
				infoBubble.setPosition(posn);
				if (!infoBubble.isOpen()) {
					infoBubble.open(map, marker);
					infoBubble.addTab("Tracker Information", content)
					infoBubble.addTab("Flyer Information", "Comming soon...")
				 }
			}
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
