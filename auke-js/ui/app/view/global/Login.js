Ext.define('Auke.view.global.Login', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.loginForm',
	bodyStyle : {
		padding : '7px'
	},
	title: 'Login as Administrator',
	initComponent : function(config) {
        var me = this;
        Ext.applyIf(me, {
            items : [  {
                xtype : 'form',
                flex : 1,
                fieldDefaults : {
                    msgTarget : 'side'
                },
                layout : {
                    pack : 'center',
                    type : 'hbox'
                },
                border : 0,
                items : [ {
                    xtype : 'container',
                    padding : '0 100 0 0',
                    border : 1,
                    title : '',
                    defaults : {
                        labelWidth : 150,
                        width : 420
                    },
                    items : [ {
                        xtype : 'textfield',
                        name : 'username',
                        fieldLabel : 'User Name',
                        allowBlank : false,
                        afterLabelTextTpl: "<span style='color:red;font-weight:bold' data-qtip='Required'>*</span>"
                    },  {
                        xtype : 'textfield',
                        inputType : 'password',
                        name : 'password',
                        fieldLabel : 'Password',
                        allowBlank : false,
                        afterLabelTextTpl: "<span style='color:red;font-weight:bold' data-qtip='Required'>*</span>"
                    } ]
                } ],
                buttonAlign : 'center',
                buttons : {
                    items : [ {
                        xtype : 'button',
                        overCls : 'btnOver',
                        text : 'Login',
                        action : 'loginBtn'
                    }, {
                        xtype : 'button',
                        action : 'clearBtn',
                        overCls : 'btnOver',
                        text : 'Clear'
                    } ]
                }
            } ]
        });
        Ext.apply(me, config);
        me.callParent(arguments);
    }
});