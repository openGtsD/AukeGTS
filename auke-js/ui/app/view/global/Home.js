Ext.define('Auke.view.global.Home', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.home',
	border: 0,
	layer: 'SIMULATED',
	myMap: '',
	title : 'Zoom Factory <span id="zoomId" style="color: red"></span>  - Number tracker in current view is <span id="trackerNumber" style="color: red"></span>',
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
			 items:[ {
                     xtype: 'combo',
                     padding: '5 0 0 0',
                     store: Ext.create('Ext.data.ArrayStore', {
                         fields: [ 'layerId' ],
                         data: [
                             ['REAL'],
                             ['SIMULATED']
                         ]
                     }),
                     displayField: 'layerId',
                     fieldLabel: 'Select Layer',
                     queryMode: 'local',
                     selectOnTab: false,
                     name: 'layerId',
                     value: 'SIMULATED',
                     listeners: {
                         select: function( combo, records, eOpts ){
                        	 me.layer = combo.getValue();
                        	 me.loadTracks(me.myMap);
                         }
                     }
	            }, {
                 xtype: 'gmappanel',
                 zoomLevel: 3,
                 gmapType: 'map',
                 height: 580,
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
                		 var map = this.getMap();
                		 me.myMap =  map;
                		 me.clearMarkers();
                		 me.loadTracks(map);
                	 }
                 }
             }]
		});
		me.callParent(arguments);
	},
	

	loadTracks : function(map){
		var me = this;
		Ext.fly("zoomId").update(map.getZoom())
		var mapBound = map.getBounds();
		var ne = mapBound.getNorthEast(); // LatLng of the north-east corner
		var sw = mapBound.getSouthWest();
		Ext.Ajax.request({
			url : Auke.utils.buildURL('drone/load-drone-in-view/', true) + this.layer + '/' + map.getZoom(),
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
				if(data.length == 0) {
					me.clearMarkers();
				}
				for (var i = 0; i < data.length; i++) {
					posn = new google.maps.LatLng(data[i].currentPosition.latitude,
							data[i].currentPosition.longitude);
					var marker = Auke.utils.createMarker(data[i].id, posn, data[i].name, Auke.utils.buildHTML(data[i]), map);
					marker.setMap(map);
					Auke.utils.markers.push(marker);
					Auke.utils.allmarkers[data[i].id] = marker;
				}
				Ext.fly("trackerNumber").update(data.length);
			}
		});
	},
	clearMarkers : function(){
		for(var i = 0; i < Auke.utils.markers.length; i++){
			var marker = Auke.utils.markers[i];
			marker.setMap(null);
		}
		Auke.utils.markers = [];
	}
});
