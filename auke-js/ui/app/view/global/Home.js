Ext.define('Auke.view.global.Home', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.home',
//	title : 'All Trackers',
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
                 	click: function(mevt){
                 		Ext.Msg.alert('Lat/Lng of Click', mevt.latLng.lat() + ' / ' + mevt.latLng.lng());
                 	//	var input = Ext.get('ac').dom,
				    			sw = new google.maps.LatLng(50.62504,-100.10742),
				    			ne = new google.maps.LatLng(50.62504,-100.10742),
				    			bounds = new google.maps.LatLngBounds(sw,ne);
				    		var options = {
				    			location: mevt.latLng,
				    			radius: '1000',
								types: ['geocode']
							};
                 	}
                 }
             }
		});
		me.callParent(arguments);
	}

});
