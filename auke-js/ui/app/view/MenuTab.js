Ext.define('Auke.view.MenuTab', {
    extend: 'Ext.tab.Tab',
    alias: 'widget.ux.menutab',
    requires: [
        'Ext.button.Split'
    ],

    /**
     * Menu align, if you need to hack the menu alignment
     */
    menuAlign: 'tl-bl?',

    constructor: function() {
        this.callParent(arguments);
        this.onClick = Ext.button.Split.prototype.onClick;
    },

    onRender: function() {
        this.callParent(arguments);

        //We change the button wrap class here! (HACK!)
        this.btnWrap.replaceCls('x-tab-arrow x-tab-arrow-right', 'x-btn-split x-btn-split-right').setStyle('padding-right', '20px');
    }
});