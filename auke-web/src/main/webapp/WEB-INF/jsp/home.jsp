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
        radius : 100, // metres
        fillColor : '#AA0000'
    });
    
    
    circle.bindTo('center', marker, 'position');
    circle.bindTo('center', marker2, 'position');
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
		        clickable: false,
	            // metres
	            radius: 100000,
	            fillColor: '#fff',
	            fillOpacity: .6,
	            strokeColor: '#313131',
	            strokeOpacity: .4,
	            strokeWeight: .8
		    });
	    	
	    	
	    	
	    	var la, lon = 0
	    	for(var i = 1; i < datas.length; i ++){
	    	  var positionUnit = datas[i];
	    	  la =positionUnit.lat; 
	    	  lon = positionUnit.lon;
	    	  var marker = new google.maps.Marker({
	    	        map : map,
	    	        position : new google.maps.LatLng(positionUnit.lat, positionUnit.lon),
	    	        title : 'The armpit of Cheshire 2'
	    	  });
	    	  circle.bindTo('center', marker, 'position');
	    	  
	    	  
	    	}
	    	console.log(circle.getBounds().contains( new google.maps.LatLng( la, lon ) ));
	    }
	});
}
google.maps.event.addDomListener(window, 'load', sendRequest);
</script>


