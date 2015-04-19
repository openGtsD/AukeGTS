Ext.define('Auke.view.global.Home', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.home',
	title : 'Zoom Factory <span id="zoomId" style="color: red"></span>  - Number tracker in current view is <span id="trackerNumber" style="color: red"></span>',
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
			 items: {
                 xtype: 'gmappanel',
                 id: 'mymap',
                 zoomLevel: 3,
                 gmapType: 'map',
                 width:'100%',
                 height: 560,
                 border: false,
                 x: 0,
                 y: 0,
                 mapConfOpts: ['enableScrollWheelZoom','enableDoubleClickZoom','enableDragging'],
                 mapControls: ['GSmallMapControl','GMapTypeControl'],
                 setCenter: {
                     lat: 50.62504,
                     lng: -100.10742
                    
                 },
                 maplisteners: {
                	 idle : function(){
                		 me.loadTracks(this.getMap());
                	 }
                 }
             }
		});
		me.callParent(arguments);
	},
	

	loadTracks : function(map){
		Ext.fly("zoomId").update(map.getZoom())
		var mapBound = map.getBounds();
		var ne = mapBound.getNorthEast(); // LatLng of the north-east corner
		var sw = mapBound.getSouthWest();
		Ext.Ajax.request({
			url : 'service/drone/load-drone-in-view/SIMULATED/'
					+ map.getZoom(),
			jsonData : JSON.stringify({
				southWestLat : sw.lat(),
				southWestLon : sw.lng(),
				northEastLat : ne.lat(),
				northEastLon : ne.lng()
			}),
			method : 'POST',
			success : function(response) {
				var res = Ext.JSON.decode(response.responseText);
				var data = res.data;
//				mgr.clearMarkers();
				var markers = [];
				for (var i = 0; i < data.length; i++) {
					posn = new google.maps.LatLng(data[i].currentPosition.latitude,
							data[i].currentPosition.longitude);
					var marker = Auke.utils.createMarker(data[i].id, posn, data[i].name,
							'/auke-js/ui/images/drone.png',
							Auke.utils.buildHTML(data[i]), map);
					marker.setMap(map);
					markers.push(marker);
//					allmarkers[data[i].id] = marker;
				}
				Ext.fly("trackerNumber").update(markers.length);
//				// mgr.addMarkers(markers, data[i].minZoom, data[i].maxZoom); TODO:
//				// its use full for show drones in zoom factory
//				mgr.addMarkers(markers, 3, 19);
//				mgr.refresh();
//				updateStatus(mgr.getMarkerCount(map.getZoom()));
				// autoCenter(markers);
			}
		});
	},

});
