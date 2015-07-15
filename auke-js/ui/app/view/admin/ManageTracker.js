Ext.define('Auke.view.admin.ManageTracker', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.manageTracker',
	title : 'Manage Tracker',
	border : 0,
	autoScroll: true,
	initComponent : function() {
		var me = this;
		Ext.apply(me, {
			items : [ {
				xtype : 'combo',
				padding : '5 0 0 0',
				store : Ext.create('Ext.data.ArrayStore', {
					fields : [ 'layerId' ],
					data : [ [ 'REAL' ], [ 'SIMULATED' ], ['SUMMARIZED'] ]
				}),
				displayField : 'layerId',
				fieldLabel : 'Select Layer',
				queryMode : 'local',
				selectOnTab : false,
				name : 'layerId',
				value: 'REAL'
				
			}, {
				xtype : 'trackerGrid',
				border : 0,
				store : 'Trackers'
			} ]
		});

		me.callParent(arguments);
	}
});