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
					itemId : 'pageHeader',
					region : 'north',
					margin : '0 5 0 0',
					items : [ {
						xtype : 'toolbar',
						items : [ {
							xtype : 'tbspacer',
							flex : 1
						}, {
							xtype : 'label',
							cls : 'fullName'
						}, {
							xtype : 'button',
							overCls : 'btnOver',
							text : 'Home',
							width : 80,
							cls : 'headerBtn',
							view : 'global.Home'
						}, {
							xtype : 'button',
							text : 'Register',
							overCls : 'btnOver',
							width : 80,
							cls : 'headerBtn',
							view : 'global.Register'
						}, {
							xtype : 'button',
							text : 'Login',
							overCls : 'btnOver',
							width : 80,
							cls : 'headerBtn',
							view : 'global.Login'
						}, {
			                xtype: 'splitbutton',
			                text: 'Administrator',
			                hidden: true,
			                overCls: 'btnOver',
			                cls: 'headerBtn',
			                menu: {
			                    xtype: 'menu',
			                    items: [
			                        {
			                            text: 'Manage Tracker',
			                            id: 'mgTracker'
			                        },
			                        '-',
			                        {
			                            text: 'Manage Layer',
			                            id: 'mgLayer'
			                        },
			                        '-',
			                        {
			                            text: 'Log Out',
			                            id: 'logout'
			                        }
			                    ]
			                }
			            },]
					} ]
				}, {
					region : 'center',
					layout : {
						type : 'vbox',
						align : 'stretch'
					},

					items : [ {
						xtype : 'container',
						id : 'viewContainer',
						overflowY : 'hidden',
						autoScroll : true,
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