/**
 * @author thaihuynh
 */
Ext.define('Auke.view.global.TrackerForm', {
    extend : 'Ext.container.Container',
    alias : 'widget.trackerForm',
    config : {
    	mode: 'Add'
    },
    initComponent : function(config) {
        var me = this;
        Ext.applyIf(me, {
            items : [  {
                xtype : 'form',
                name : 'trackerForm',
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
                    padding : '5 99 0 0',
                    border : 1,
                    defaults : {
                        labelWidth : 175,
                        width : 420
                    },
                    items : [ {
                        xtype : 'textfield',
                        name : 'id',
                        fieldLabel : 'Tracker ID(IMEI/ESN Number)',
                        allowBlank : false,
                        afterLabelTextTpl: "<span style='color:red;font-weight:bold' data-qtip='Required'>*</span>"
                    } , {
                        xtype : 'textfield',
                        name : 'name',
                        fieldLabel : 'Name',
                        hidden: true
                    }, {
                        xtype : 'textfield',
                        name : 'simPhone',
                        fieldLabel : 'SIM Phone',
                        hidden: true
                    }, {
                        xtype : 'datefield',
                        name : 'createDate',
                        fieldLabel : 'Create Date',
                        submitFormat: 'Y-m-d',
                        hidden: true
                    }
                    ]
                } ],
                buttonAlign : 'center',
                buttons : {
                    items : [ {
                        xtype : 'button',
                        overCls : 'btnOver',
                        text : me.mode == 'Info' ? 'Get Tracker' : me.mode,
                        action : me.mode
                    }, {
                        xtype : 'button',
                        action : 'clearBtn',
                        overCls : 'btnOver',
                        text : 'Clear',
                        hidden: me.mode == 'Delete' || me.mode == 'Info'
                    } ]
                }
            } ]
        });
//        Ext.apply(me, config);
        me.callParent(arguments);
    }
});