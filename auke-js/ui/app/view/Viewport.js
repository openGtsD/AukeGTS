Ext.define('Auke.view.Viewport', {
	extend : 'Ext.container.Viewport',
	id : 'viewport',
	alias : 'widget.aukeViewport',
	layout: {
	    type: 'vbox',
	    align : 'center',
	    pack  : 'start',
	},
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
			items : [ {
				xtype : 'container',
				itemId : 'parentContainer',
				width: 980,
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
						}, {
							xtype : 'button',
							text : 'Update Trackers',
							overCls : 'btnOver',
							cls : 'headerBtn',
							view : 'global.Update'
						}, {
							xtype : 'button',
							text : 'Delete Trackers',
							overCls : 'btnOver',
							cls : 'headerBtn',
							view : 'global.Delete'
						}, {
							xtype : 'button',
							text : 'RSS Feed',
							overCls : 'btnOver',
							cls : 'headerBtn',
							view : 'global.RssFeed'
						}, {
							xtype : 'button',
							text : 'View All Trackers',
							overCls : 'btnOver',
							cls : 'headerBtn',
							view : 'admin.ManageTracker'
						}, {
							xtype : 'button',
							text : 'Login as Administrator',
							overCls : 'btnOver',
							cls : 'headerBtn',
							view : 'global.Login',
							action : 'loginBtn',
							hidden : true
						// TODO remove when apply authen
						}, {
							xtype : 'splitbutton',
							text : 'Administrator',
							hidden : true,
							overCls : 'btnOver',
							cls : 'headerBtn',
							menu : {
								xtype : 'menu',
								items : [ {
									text : 'Manage Tracker',
									id : 'mgTracker',
									view : 'admin.ManageTracker'
								}, '-', {
									text : 'Manage Layer',
									id : 'mgLayer',
									view : 'admin.ManageLayer'
								}, '-', {
									text : 'Log Out',
									id : 'logout'
								} ]
							}
						}, ]
					} ]
				}, {
					region : 'center',
					
					items : [ {
						xtype : 'container',
						id : 'viewContainer'
					} ]
				} ]
			} ]
		});

		me.callParent(arguments);
	}

});