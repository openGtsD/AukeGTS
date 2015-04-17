Ext.define('Auke.view.Viewport', {
	extend : 'Ext.container.Viewport',
	id : 'viewport',
	alias : 'widget.aukeViewport',
	layout : {
		type : 'fit'
	},

	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
			items : [ {
				xtype : 'container',
				itemId : 'parentContainer',
				minWidth : 1000,

				items : [ {
					xtype : 'container',
					loader : {
						url : 'service/ui/header',
						autoLoad : true,
						renderer : 'component'
					},
					itemId : 'pageHeader',
					region : 'north',
					margin : '0 5 0 0'
				}, {
					region : 'center',
					layout : {
						type : 'vbox',
						align : 'stretch'
					},

					items : [ {
						xtype : 'container',
						id : 'viewContainer',
						overflowY:'hidden',
						autoScroll: true,
						layout : {
							type : 'fit'
						}
					} ]
				}, {
					xtype : 'pageFooter',
					itemId : 'pageFooter',
					region : 'south'
				} ]
			} ]
		});

		me.callParent(arguments);
	}

});