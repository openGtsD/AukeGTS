<!DOCTYPE html>
<html>
  <head>
    <style>
      #map-canvas {
        width: 100%;
        height: 988px;
      }
    </style>
    <script src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
  </head>
  <body>
    <div id="map-canvas"></div>
  </body>
</html>


<script>

function initCenter(lat, lon, zoom){
	 var mapCenter = new google.maps.LatLng(lat, lon);
	 var map = new google.maps.Map(document.getElementById('map-canvas'), {
	        'zoom' : zoom,
	        'center' : mapCenter,
	        'mapTypeId' : google.maps.MapTypeId.ROADMAP
	 });
	
	return map;
}


function showCircle() {
    var map = initCenter(52, -2, 7)

    var marker = new google.maps.Marker({
        map : map,
        position : new google.maps.LatLng(53.1, -2.44),
        title : 'The armpit of Cheshire'
    });
    
    var marker2 = new google.maps.Marker({
        map : map,
        position : new google.maps.LatLng(53.1, -2.77),
        title : 'The armpit of Cheshire 2'
    });

    // Add circle overlay and bind to marker
    var circle = new google.maps.Circle({
        map : map,
        radius : 100000, // metres
        fillColor : '#AA0000'
    });
    
    
    circle.bindTo('center', marker, 'position');
    circle.bindTo('center', marker2, 'position');
}

function showRectangle() {
	
	 var map = initCenter(33.678176, -116.242568, 13)

	  var rectangle = new google.maps.Rectangle({
	    strokeColor: '#FF0000',
	    strokeOpacity: 0.8,
	    strokeWeight: 2,
	    fillColor: '#FF0000',
	    fillOpacity: 0.35,
	    map: map,
	    bounds: new google.maps.LatLngBounds(
	      new google.maps.LatLng(33.671068, -116.25128),
	      new google.maps.LatLng(33.685282, -116.233942))
	  });
	 
	  var marker = new google.maps.Marker({
	        map : map,
	        position : new google.maps.LatLng(33.671068, -116.25128),
	        title : 'The armpit of Cheshire'
	    });
	 
	  var marker2 = new google.maps.Marker({
	        map : map,
	        position : new google.maps.LatLng(33.685282, -116.233942),
	        title : 'The armpit of Cheshire 2'
	   });
	  
	  var marker3 = new google.maps.Marker({
	        map : map,
	        position : new google.maps.LatLng(33.685282, -116.251267),
	        title : 'The armpit of Cheshire 3'
	   });
	  
	  var marker4 = new google.maps.Marker({
	        map : map,
	        position : new google.maps.LatLng(33.685282, -116.236642),
	        title : 'The armpit of Cheshire 4'
	   });
	    
	  rectangle.bindTo('center', marker, 'position');
	  rectangle.bindTo('center', marker2, 'position');
	  rectangle.bindTo('center', marker3, 'position');
	  rectangle.bindTo('center', marker4, 'position');
}

function sendRequest(){
	$.ajax({
	    url: 'service/feed/get-feed',
	    dataType: 'json',
	    contentType: "text/html; charset=utf-8",
	    success: function(response) {
	    	var datas = response.data;
	    	var center = datas[0];// Just for test;
	    	var map = initCenter(center.lat, center.lon, 8);
	    	var circle = new google.maps.Circle({
		        map : map,
		        radius : 100000, // metres
		        fillColor : '#AA0000'
		    });
	    	for(var i = 1; i < datas.length; i ++){
	    	  var positionUnit = datas[i];
	    	  var marker = new google.maps.Marker({
	    	        map : map,
	    	        position : new google.maps.LatLng(positionUnit.lat, positionUnit.lon),
	    	        title : 'The armpit of Cheshire 2'
	    	  });
	    	  circle.bindTo('center', marker, 'position');
	    	  
	    	}
	    }
	});
}
google.maps.event.addDomListener(window, 'load', sendRequest);
</script>


