Ext.define('Auke.view.admin.ManageTracker', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.manageTracker',
	title : 'Manage Tracker',
	border : 0,

	initComponent : function() {
		var me = this;
		Ext.apply(me, {
			items : [ {
				xtype : 'combo',
				padding : '5 0 0 0',
				store : Ext.create('Ext.data.ArrayStore', {
					fields : [ 'layerId' ],
					data : [ [ 'REAL' ], [ 'SIMULATED' ] ]
				}),
				displayField : 'layerId',
				fieldLabel : 'Select Layer',
				queryMode : 'local',
				selectOnTab : false,
				name : 'layerId'
			}, {
				xtype : 'trackerGrid',
				border : 0,
				store : 'Trackers',
				height: 580
			} ]
		});

		me.callParent(arguments);
	}
});