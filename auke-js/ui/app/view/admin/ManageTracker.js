Ext.define('Auke.view.admin.ManageTracker', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.manageTracker',
	layout : {
		align : 'stretch',
		type : 'vbox'
	},
	title : 'Manage Tracker',
	initComponent : function() {
		var me = this;
		Ext.apply(me, {
			items : [ {
				xtype : 'trackerForm',
				height : 300
			}, {
				xtype : 'trackerGrid',
				store : 'Trackers',
				maxHeight : 350,
				minHeight : 350
			} ]
		});

		me.callParent(arguments);
	}
});