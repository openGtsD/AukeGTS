function initCenter(lat, lon, zoom){
	 var mapCenter = new google.maps.LatLng(lat, lon);
	 var map = new google.maps.Map(document.getElementById('map-canvas'), {
	        'zoom' : zoom,
	        'center' : mapCenter,
	        'mapTypeId' : google.maps.MapTypeId.ROADMAP,
	         'mapTypeControl': false
	 });
	
	return map;
}

function addInfoWindow(marker, message, map) {
   var infoWindow = new google.maps.InfoWindow({
       content: message
   });
   google.maps.event.addListener(marker, 'click', function () {
       infoWindow.open(map, marker);
   });
}


function showCircle() {
	 var contentCenter = '<span class="infowin">Center Marker (draggable)</span>';
    var contentA = '<span class="infowin">Marker A (draggable)</span>';
    var contentB = '<span class="infowin">Marker B (draggable)</span>';
    
    
    latLngA = new google.maps.LatLng(37.2, -94.1);
    latLngB = new google.maps.LatLng(38, -93);
    
    var latLngCenter = new google.maps.LatLng(37.081476, -94.510574);
    map = new google.maps.Map(document.getElementById('map-canvas'), {
        zoom: 7,
        center: latLngCenter,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        mapTypeControl: false
    }),
    
    latLngCMarker = new google.maps.LatLng(37.0814, -94.5105);
    markerCenter = new google.maps.Marker({
        position: latLngCMarker,
        title: 'Location',
        map: map,
        draggable: true
    }),
    infoCenter = new google.maps.InfoWindow({
        content: contentCenter
    }),
    markerA = new google.maps.Marker({
        position: latLngA,
        title: 'Location',
        map: map,
        draggable: true
    }),
    infoA = new google.maps.InfoWindow({
        content: contentA
    }),
    markerB = new google.maps.Marker({
        position: latLngB,
        title: 'Location',
        map: map,
        draggable: true
    }),
    infoB = new google.maps.InfoWindow({
        content: contentB
    })
// // exemplary setup:
// // Assumes that your map is signed to the var "map"
// // Also assumes that your marker is named "marker"
// ,
    circle = new google.maps.Circle({
        map: map,
        clickable: false,
        // metres
        radius: 100000,
        fillColor: '#fff',
        fillOpacity: .6,
        strokeColor: '#313131',
        strokeOpacity: .4,
        strokeWeight: .8
    });
// // attach circle to marker
   circle.bindTo('center', markerCenter, 'position');
//
// // get the Bounds of the circle
   var bounds = circle.getBounds(),
    noteA = jQuery('.bool#a'),
    noteB = jQuery('.bool#b');
//
noteA.text(bounds.contains(latLngA));
noteB.text(bounds.contains(latLngB));
//
// // get some latLng object and Question if it's contained in the circle:
google.maps.event.addListener(markerCenter, 'dragend', function() {
	 latLngCenter = new google.maps.LatLng(markerCenter.position.lat(),
	 markerCenter.position.lng());
	 bounds = circle.getBounds();
	 noteA.text(bounds.contains(latLngA));
	 noteB.text(bounds.contains(latLngB));
});
//
google.maps.event.addListener(markerA, 'dragend', function() {
latLngA = new google.maps.LatLng(markerA.position.lat(),
markerA.position.lng());
noteA.text(bounds.contains(latLngA));
});
//
google.maps.event.addListener(markerB, 'dragend', function() {
latLngB = new google.maps.LatLng(markerB.position.lat(),
markerB.position.lng());
noteB.text(bounds.contains(latLngB));
});
//
google.maps.event.addListener(markerCenter, 'click', function() {
    infoCenter.open(map, markerCenter);
});
google.maps.event.addListener(markerA, 'click', function() {
    infoA.open(map, markerA);
});
google.maps.event.addListener(markerB, 'click', function() {
    infoB.open(map, markerB);
});
//
google.maps.event.addListener(markerCenter, 'drag', function() {
infoCenter.close();
noteA.html("draggin&hellip;");
noteB.html("draggin&hellip;");
});
//
// google.maps.event.addListener(markerA, 'drag', function() {
// infoA.close();
// noteA.html("draggin&hellip;");
// });
//
// google.maps.event.addListener(markerB, 'drag', function() {
// infoB.close();
// noteB.html("draggin&hellip;");
// });
}


function sendRequest(){
	$.ajax({
	    url: 'service/feed/get-feed',
	    dataType: 'json',
	    contentType: "text/html; charset=utf-8",
	    success: function(response) {
	    	var datas = response.data;
	    	var center = datas[0];// Just for test;
	    	var map = initCenter(center.lat, center.lon, 7);
	    	var circle = new google.maps.Circle({
		        map : map,
		        clickable: false,
	            // metres
	            radius: 100000,
	            fillColor: '#fff',
	            fillOpacity: .6,
	            strokeColor: '#313131',
	            strokeOpacity: .4,
	            strokeWeight: .8
		    });
	    	
	    	var mCenter = datas[1];
	    	latLngCMarker = new google.maps.LatLng(mCenter.lat, mCenter.lon);
	        markerCenter = new google.maps.Marker({
	             position: latLngCMarker,
	             title: 'Marker Center',
	             map: map,
	             draggable: true,
	             icon: 'http://maps.google.com/mapfiles/ms/icons/green-dot.png'
	         });
	        circle.bindTo('center', markerCenter, 'position');
	    	for(var i = 2; i < datas.length; i ++){
	    	  var positionUnit = datas[i];
	    	  var marker = new google.maps.Marker({
	    	        map : map,
	    	        position : new google.maps.LatLng(positionUnit.lat, positionUnit.lon),
	    	        title : positionUnit.id
	    	  });
	    	  addInfoWindow(marker, marker.title, map);
	    	}
	    }
	});
}

google.maps.event.addDomListener(window, 'load', sendRequest);
