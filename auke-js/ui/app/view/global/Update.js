Ext.define('Auke.view.global.Update', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.update',
	layout : {
		align : 'stretch',
		type : 'vbox'
	},
	title : 'Update Your Trackers',
	border: 0,
	initComponent : function() {
		var me = this;
		Ext.apply(me, {
			items : [ {
				xtype : 'trackerForm',
				mode: 'Info'
			} ]
		});
		me.callParent(arguments);
	}
});