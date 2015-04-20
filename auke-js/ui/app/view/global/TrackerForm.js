/**
 * @author thaihuynh
 */
Ext.define('Auke.view.global.TrackerForm', {
	extend : 'Ext.panel.Panel',
    extend : 'Ext.container.Container',
    alias : 'widget.trackerForm',

    style : 'border: 1px solid #ccc; background-color: #ffffff; padding-top: 5px',
    bodyStyle : 'background-color: #ffffff;',

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
                    padding : '0 100 0 0',
                    border : 1,
                    title : '',
                    defaults : {
                        labelWidth : 150,
                        width : 420
                    },
                    items : [
                             {
                        xtype : 'textfield',
                        name : 'id',
                        fieldLabel : 'Tracker ID (Read Only)',
                        readOnly: true
                       // allowBlank : false,
                        //afterLabelTextTpl: "<span style='color:red;font-weight:bold' data-qtip='Required'>*</span>"
                    },{
                        xtype: 'combo',
                        store: Ext.create('Ext.data.ArrayStore', {
                            fields: [ 'layerId' ],
                            data: [
                                ['REAL'],
                                ['SIMULATED']
                            ]
                        }),
                        displayField: 'layerId',
                        fieldLabel: 'Layer ID',
                        queryMode: 'local',
                        selectOnTab: false,
                        name: 'layerId',
                        allowBlank : false,
                        afterLabelTextTpl: "<span style='color:red;font-weight:bold' data-qtip='Required'>*</span>"
                    }, {
                        xtype : 'textfield',
                        name : 'name',
                        fieldLabel : 'Name',
                        allowBlank : false,
                        afterLabelTextTpl: "<span style='color:red;font-weight:bold' data-qtip='Required'>*</span>"
                    },{
                        xtype : 'textfield',
                        name : 'imei',
                        fieldLabel : 'IMEI/ESN Number',
                        allowBlank : false,
                        afterLabelTextTpl: "<span style='color:red;font-weight:bold' data-qtip='Required'>*</span>"
                    }, {
                        xtype : 'textfield',
                        name : 'simPhone',
                        fieldLabel : 'SIM Phone',
                        allowBlank : false,
                        afterLabelTextTpl: "<span style='color:red;font-weight:bold' data-qtip='Required'>*</span>"
                    }
//                    , {
//                    	 xtype: 'combo',
//                         store: Ext.create('Ext.data.ArrayStore', {
//                             fields: [ 'active' ],
//                             data: [
//                                 ['Yes'],
//                                 ['No']
//                             ]
//                         }),
//                         displayField: 'active',
//                         fieldLabel: 'Active',
//                         queryMode: 'local',
//                         selectOnTab: false,
//                         name: 'active',
//                         allowBlank : false,
//                         afterLabelTextTpl: "<span style='color:red;font-weight:bold' data-qtip='Required'>*</span>"
//                    }
                    ]
                } ],
                buttonAlign : 'center',
                buttons : {
                    items : [ {
                        xtype : 'button',
                        overCls : 'btnOver',
                        text : 'Save',
                        action : 'saveBtn'
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