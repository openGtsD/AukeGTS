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
                html : 'Copyright &copy; 2015 Auke Team.'
            }, {
                xtype: 'label',
                cls  : 'version',
                html : ''
            } ]
        });

        me.callParent(arguments);
    }

});
