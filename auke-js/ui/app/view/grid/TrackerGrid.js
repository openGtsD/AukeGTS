Ext.define('Auke.view.grid.TrackerGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.trackerGrid',
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
			columns : [ {
				dataIndex : 'id',
				text : 'ID',
				flex : 1
			}, {
				dataIndex : 'layerId',
				text : "Layer ID",
				flex : 1
			}, {
				dataIndex : 'name',
				text : "Name",
				flex : 1
			}, {
				dataIndex : 'imei',
				text : "IMEI/ESN Number",
				flex : 1
			}, {
				dataIndex : 'sim',
				text : "SIM Phone",
				flex : 1
			},
//			{
//				dataIndex : 'active',
//				text : "Active",
//				flex : 1
//			}, 
			{
				dataIndex : 'createDate',
				text : "Create Date",
				flex : 1
			},  {
				dataIndex : 'modifiedDate',
				text : "Modify Date",
				flex : 1
			},{
                menuDisabled : true,
                sortable : false,
                xtype : 'actioncolumn',
                dataIndex: 'actionColumn',
                flex: 0,
                width: 160,
                minWidth : 150,
                renderer : this.renderButton,
                resizable: false
            }, ]
		});
		me.callParent(arguments);
	},
	
	renderButton : function(value, metaData, record, rowIndex, colIndex, store){
        return '<button type="button" class="action_button" actionType="Edit">' + 'Edit' 
            + '</button></span>&nbsp;&nbsp;<button type="button" class="action_button"" actionType="Delete">'  +  'Delete' + '</button>';
    }
});
