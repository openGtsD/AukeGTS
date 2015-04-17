Ext.define('Auke.view.global.Home', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.home',
	title : 'All Trackers',
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
			items : [ {
				xtype : 'trackerGrid',
				store : 'Trackers',
				maxHeight : 500,
				minHeight : 500
			} ]
		});
		me.callParent(arguments);
	}

});
