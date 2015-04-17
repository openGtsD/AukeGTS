Ext.define('Auke.view.global.Register', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.register',
	layout : {
		align : 'stretch',
		type : 'vbox'
	},
	title : 'Register Tracker',
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