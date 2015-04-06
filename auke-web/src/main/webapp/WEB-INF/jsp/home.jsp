<!DOCTYPE html>
<html>
  <head>
    <style>
      #map-canvas {
        width: 100%;
        height: 988px;
      }
    </style>
   <script src="http://maps.googleapis.com/maps/api/js">
</script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js"></script>
    <script type="text/javascript" src="/auke-js/resources/js/auke.js"></script>
    
  </head>
  <body>
	<div class="info" style="font-weight: bold; font-size: 20pt;">Map Center: <span style="color: red" id="resultCenter"></span></div>
  	<div class="info" style="font-weight: bold; font-size: 20pt;">Zoom Factory: <span style="color: red" id="resultZoom"></span></div>
  	<div class="info" style="font-weight: bold; font-size: 20pt;"><span style="color: red" id="resultBoundary"></span></div>
	<div class="info" style="font-weight: bold; font-size: 20pt;">Number drone in current view is: <span style="color: red" id="numberDrone"></span></div>
  	
    <div id="map-canvas"></div>
  </body>
</html>


