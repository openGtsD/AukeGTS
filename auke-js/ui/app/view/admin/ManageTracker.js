Ext.define('Auke.view.admin.ManageTracker', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.manageTracker',
	title : 'Manage Tracker',
	border: 0,
	initComponent : function() {
		var me = this;
		Ext.apply(me, {
			items : [
//			         {
//				xtype : 'trackerForm',
//				height : 300
//			}, 
			{
				xtype : 'trackerGrid',
				store : 'Trackers'
			} ]
		});

		me.callParent(arguments);
	}
});