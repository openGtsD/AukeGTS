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

function init() {

    var mapCenter = new google.maps.LatLng(52, -2);
    var map = new google.maps.Map(document.getElementById('map-canvas'), {
        'zoom' : 7,
        'center' : mapCenter,
        'mapTypeId' : google.maps.MapTypeId.ROADMAP
    });

    //
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

google.maps.event.addDomListener(window, 'load', init);
</script>


