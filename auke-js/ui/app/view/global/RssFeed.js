Ext.define('Auke.view.global.RssFeed', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.rssFeed',
	title : 'RSS Feeds',
	border : 0,
	initComponent : function() {
		var me = this;
		Ext.apply(me, {
			items : [ {
				xtype : 'textareafield',
				width : '100%',
				layout : 'fit',
				height : 580,
			} ]
		});

		me.callParent(arguments);
	}
});