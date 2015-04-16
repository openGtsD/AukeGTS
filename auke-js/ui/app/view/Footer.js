Ext.define('Auke.view.Footer', {
    extend : 'Ext.toolbar.Toolbar',
    alias : 'widget.pageFooter',
    border : 0,

    initComponent : function() {
        var me = this;

        Ext.applyIf(me, {
            items : [ {
                xtype : 'tbspacer',
                flex : 1
            }, {
                xtype : 'label',
                html : 'Test.'
            }, {
                xtype: 'label',
                html : 'Powered by AukeTeam'
            }, {
                xtype: 'label',
                cls  : 'version',
                html : ''
            } ]
        });

        me.callParent(arguments);
    }

});
