Ext.ns('Auke.utils');
Auke.utils.mgr = "";
Auke.utils.gmappanel = "";
Auke.utils.markers = [];
Auke.utils.allmarkers = {};
// Change url when go live
Auke.utils.baseURL = "http://89.221.242.66:8888/";
Auke.utils.serviceURL = "http://89.221.242.66:8080/";

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
	viewContainer.doLayout();
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
			+ "<li>Moving: <span class='highlight'>" + data.moving
			+ "</span></li> <li>Name: <span class='highlight'>" + data.name
			+ "</span></li><li>Time: <span class='highlight'>"
			+ new Date(data.currentPosition.time * 1000) + "</li></ul>"
};

Auke.utils.createMarker = function(id, posn, title, numtrackers, layerId, map, icon) {
	var ico = icon == null ? (map.getZoom() >= 11 ? Auke.utils.baseURL
			+ "auke-js/ui/images/flight.gif" : "") : icon;
	var markerOptions = {
		id : id,
		position : posn,
		title : title + " Number Tracker: " + numtrackers,
		icon : ico
	};
	var marker = new google.maps.Marker(markerOptions);
	Auke.utils.createInfoWindow(marker, map, layerId);
	Auke.utils.centerZoom(marker, map);
	return marker;
}

Auke.utils.makeNewInfoBuddle = function() {
	var info = new InfoBubble({
		shadowStyle : 1,
		padding : 10,
		borderRadius : 5,
		minWidth : 200,
		borderWidth : 1,
		disableAutoPan : true,
		hideCloseButton : false
	});
	return info;
}

infoBubble = Auke.utils.makeNewInfoBuddle();
Auke.utils.createInfoWindow = function(marker, map, layerId) {
	google.maps.event.addListener(marker, 'click', function() {
		Auke.utils.reMakeInfoBubble();
		if (map.getZoom() >= 11) {
			Auke.utils.getTracker(marker, map);
		} else {
			Auke.utils.getIncludedTracker(map, marker, layerId);
		}
	});
}

Auke.utils.reMakeInfoBubble = function() {
	if (infoBubble) {
		infoBubble.close();
		delete infoBubble;
		infoBubble = Auke.utils.makeNewInfoBuddle();
	}
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

Auke.utils.getTracker = function(marker, map) {
	Ext.Ajax.request({
		url : Auke.utils.buildURL('drone/get-tracker/', true) + marker.id,
		method : 'POST',
		success : function(response) {
			var res = Ext.JSON.decode(response.responseText);
			if (res.success) {
				var content = Auke.utils.buildHTML(res.data, false);
				Auke.utils.reMakeInfoBubble();
					infoBubble.open(map, marker);
					infoBubble.addTab("Tracker Information", content)
					infoBubble.addTab("Flyer Information", "Comming soon...")
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

Auke.utils.makeLink = function (value) {
    return Auke.utils.isEmpty(value) ? "" : '<span class="my-link">' + Ext.util.Format.htmlEncode(value) + '</span>';
};

Auke.utils.isEmpty = function (value) {
    return (value == null || value == "" || value.toString().trim() == "" || value == 'undefined' || value.length == 0)
};

Auke.utils.changeMarkerPosition = function(data) {
	var newPosition = new google.maps.LatLng(data.currentPosition.latitude,
			data.currentPosition.longitude);
	var marker = Auke.utils.allmarkers[data.id];
	marker.setPosition(newPosition);
}

google.maps.event.addDomListener(window, "resize", function() {
	if(!Auke.utils.isEmpty(Auke.utils.mgr)) {
		//$('#map_canvas').css("height",$(window).height());
	     //$('#map_canvas').css("width",$(window).width());
		var gmappanel = Auke.utils.gmappanel;
		gmappanel.height = Ext.getBody().getViewSize().height;
		gmappanel.width = Ext.getBody().getViewSize().width;
		var map = Auke.utils.mgr.map_;
	    var center = map.getCenter();
	    google.maps.event.trigger(map, "resize");
	    map.setCenter(center); 
	}
});
