Ext.define('Auke.view.Viewport', {
    extend:'Ext.container.Viewport',
    id:'viewport',
    alias: 'widget.emViewport',
    layout:{
        type:'fit'
    },

    initComponent:function () {
        var me = this;
        Ext.applyIf(me, {
            items:[
                {
                    xtype:'container',
                    itemId:'parentContainer',
                    minWidth : 1000,
                    
                    items:[
                        {
                            xtype:'container',
                            itemId: 'pageHeader',
                            region:'north',
                            margin: '0 5 0 0'
                        },
                        {
                            region:'center',
                            layout:{
                                type:'vbox',
                                align:'stretch'
                            },

                            items:[
                                {
                                    xtype: 'tabpanel',
                                    cls : 'MainNav',
                                    bodyStyle : 'display: none',
                                    autoGenId : false,
                                    plain: true,
                                    id:'mainNav',
                                    tabBar: {
                                        layout: {
                                            pack : 'end'
                                        }
                                    }
                                },
                                {
                                    xtype:'container',
                                    id:'viewContainer',
                                    flex:1,
                                    title:'&nbsp;',
                                    overflowY:'hidden',
                                    layout:{
                                        type:'fit'
                                    }
                                }
                            ]
                        },
                        {
                            xtype:  'pageFooter',
                            itemId: 'pageFooter',
                            region: 'south'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }

});