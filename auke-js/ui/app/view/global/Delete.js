Ext.define('Auke.view.global.Delete', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.delete',
	layout : {
		align : 'stretch',
		type : 'vbox'
	},
	title : 'Delete Your Trackers',
	border: 0,
	initComponent : function() {
		var me = this;
		Ext.apply(me, {
			items : [ {
				xtype : 'trackerForm',
				mode: 'Delete'
			} ]
		});
		me.callParent(arguments);
	}
});