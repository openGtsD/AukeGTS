Ext.define('Auke.view.global.Update', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.update',
    layout : {
        align : 'stretch',
        type : 'vbox'
    },
    title: 'Update Tracker',
    initComponent : function() {
        var me = this;
        Ext.apply(me, {
            items : [ {
                xtype : 'trackerForm',
                height: 300
            } ]
        });

        me.callParent(arguments);
    }
});