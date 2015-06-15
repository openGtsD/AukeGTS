Ext.define('Auke.view.grid.TrackerGrid', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.trackerGrid',
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
			columns : [ {
				dataIndex : 'id',
				text : 'Tracker ID',
				flex : 1,
				renderer : Auke.utils.makeLink
			}, {
				dataIndex : 'name',
				text : "Name",
				flex : 1
			}, {
				dataIndex : 'simPhone',
				text : "SIM Phone",
				flex : 1
			},  {
				dataIndex : 'lon',
				text : "Longitude",
				flex : 1
			},  {
				dataIndex : 'lat',
				text : "Lagitude",
				flex : 1
			}, {
				dataIndex : 'alt',
				text : "Altitude",
				flex : 1
			},	
			{
				dataIndex : 'moving',
				text : "Moving",
				flex : 1
			}, 
			{
				dataIndex : 'active',
				text : "Active",
				flex : 1
			}, 
			{
				dataIndex : 'createDate',
				text : "Create Date",
				flex : 1,
				renderer : function(value, meta, record) {
					return Ext.util.Format.date(value, 'm/d/Y H:i:s');
				}
			},  {
				dataIndex : 'modifiedDate',
				text : "Modify Date",
				flex : 1,
				renderer : function(value, meta, record) {
					return Ext.util.Format.date(value, 'm/d/Y H:i:s');
				}
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
        return '</button></span>&nbsp;&nbsp;<button type="button" class="action_button"" actionType="Delete">'  +  'Delete' + '</button>';
    }
	
//	renderButton : function(value, metaData, record, rowIndex, colIndex, store){
//        return '<button type="button" class="action_button" actionType="Edit">' + 'Edit' 
//            + '</button></span>&nbsp;&nbsp;<button type="button" class="action_button"" actionType="Delete">'  +  'Delete' + '</button>';
//    }
});
