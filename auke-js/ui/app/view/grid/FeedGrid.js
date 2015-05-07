Ext.define('Auke.view.grid.FeedGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.feedGrid',
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
			columns : [ {
				dataIndex : 'url',
				text : 'Links',
				flex : 1,
				renderer : function(value, metaData, record, rowIndex,
						colIndex, store, view) {
					return '<span class="my-link">' + Ext.util.Format.htmlEncode(value) + '</span>'
				}
			} ]
		});
		me.callParent(arguments);
	}
});
