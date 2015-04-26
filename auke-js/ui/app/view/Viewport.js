Ext.define('Auke.view.Viewport', {
	extend : 'Ext.container.Viewport',
	id : 'viewport',
	alias : 'widget.aukeViewport',
	layout : {
		 pack : 'center',
         type : 'hbox'
	},

	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
			items : [ {
				xtype : 'container',
				itemId : 'parentContainer',
				width: 990,
				items : [ {
					xtype : 'container',
					itemId : 'pageHeader',
					region : 'north',
					items : [ {
						xtype : 'toolbar',
						items : [ {
							xtype : 'button',
							overCls : 'btnOver',
							text : 'Home',
							cls : 'headerBtn',
							view : 'global.Home'
						}, {
							xtype : 'button',
							text : 'Register Trackers',
							overCls : 'btnOver',
							cls : 'headerBtn',
							view : 'global.Register'
						},  {
							xtype : 'button',
							text : 'Update Trackers',
							overCls : 'btnOver',
							cls : 'headerBtn',
							view : 'global.Update'
						},  {
							xtype : 'button',
							text : 'Delete Trackers',
							overCls : 'btnOver',
							cls : 'headerBtn',
							view : 'global.Delete'
						}, {
							xtype : 'tbspacer',
							flex : 1
						}, {
							xtype : 'button',
							text : 'Login as Administrator',
							overCls : 'btnOver',
							cls : 'headerBtn',
							view : 'global.Login',
							action: 'loginBtn'
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
			                            id: 'mgTracker',
			                            view: 'admin.ManageTracker'
			                        },
			                        '-',
			                        {
			                            text: 'Manage Layer',
			                            id: 'mgLayer',
			                            view: 'admin.ManageLayer'
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
//						overflowY : 'hidden',
//						autoScroll : true,
						layout : {
							type : 'fit'
						}
					} ]
				} ]
			} ]
		});

		me.callParent(arguments);
	}

});