Ext.define('Auke.view.global.Register', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.register',
	layout : {
		align : 'stretch',
		type : 'vbox'
	},
	title : 'Register Trackers',
	border: 0,
	initComponent : function() {
		var me = this;
		Ext.apply(me, {
			items : [ {
				xtype : 'trackerForm',
				mode: 'Add'
			} ]
		});
		me.callParent(arguments);
	}
});