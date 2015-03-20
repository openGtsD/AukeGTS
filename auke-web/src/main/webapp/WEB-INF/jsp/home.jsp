<!DOCTYPE html>
<html>
  <head>
    <style>
      #map-canvas {
        width: 100%;
        height: 988px;
      }
    </style>
    <script src="https://maps.googleapis.com/maps/api/js"></script>
  </head>
  <body>
    <div id="map-canvas"></div>
  </body>
</html>

<script>
 function initialize() {
    var mapCanvas = document.getElementById('map-canvas');
    var mapOptions = {
      center: new google.maps.LatLng(44.5403, -78.5463),
      zoom: 8,
      mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    var map = new google.maps.Map(mapCanvas, mapOptions);
  }
  
  google.maps.event.addDomListener(window, 'load', initialize);
</script>

