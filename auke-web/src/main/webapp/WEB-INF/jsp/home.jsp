<!DOCTYPE html>
<html>
  <head>
    <style>
      #map-canvas {
        width: 100%;
        height: 900px;
      }
    </style>
   <script src="http://maps.googleapis.com/maps/api/js">
</script>
    <script type="text/javascript" src="/auke-js/resources/js/jquery.min.js"></script>
	<script type="text/javascript" src="/auke-js/resources/js/markermanager-mini.js"></script>
    <script type="text/javascript" src="/auke-js/resources/js/auke.js"></script>
    
  </head>
  <body>
    <div class="info" style="font-weight: bold; font-size: 20pt;"> Zoom Factory: <span style="color: red" id="resultZoom"></span> - Number drone in current view is: <span style="color: red" id="numberDrone"></span>  <input type="button" onclick="reloadMarkers()" value="reload all drone" /> </div>
    <div class="info" style="font-weight: bold; font-size: 20pt;"><span style="color: red" id="resultBoundary"></span></div>
    <div id="map-canvas"></div>
  </body>
</html>


