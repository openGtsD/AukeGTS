Ext.define('Auke.view.global.RssFeed', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.rssFeed',
	title : 'RSS Feeds',
	border : 0,
	initComponent : function() {
		var me = this;
		Ext.apply(me, {
			items : [  {
				xtype : 'feedGrid',
				border : 0,
				store: 'Feeds'
			} ]
		});

		me.callParent(arguments);
	}
});