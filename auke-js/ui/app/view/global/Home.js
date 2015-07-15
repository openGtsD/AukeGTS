Ext.define('Auke.view.global.Home', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.home',
	border: 0,
	layer: 'SIMULATED',
	title : 'Zoom Factory <span id="zoomId" style="color: red"></span>  - Number <span id="type"></span> in current view is <span id="trackerNumber" style="color: red"></span>',
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
                     value: 'SIMULATED'
	            }, {
                 xtype: 'gmappanel',
                 zoomLevel: 3,
                 gmapType: 'map',
                 cls: 'reset-box-sizing',
                 border: false,
                 x: 0,
                 y: 0,
                 mapConfOpts: ['enableScrollWheelZoom','enableDoubleClickZoom','enableDragging'],
                 mapControls: ['GSmallMapControl','GMapTypeControl'],
                 setCenter: {
                     lat: 59.913869,
                     lng: 10.752245
                    
                 }
             }]
		});
		me.callParent(arguments);
	},
	
	setLayer : function(layer) {
		this.layer = layer;
	},
	
	getLayer : function() {
		return this.layer;
	}
});
