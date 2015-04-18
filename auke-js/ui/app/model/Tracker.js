Ext.define("Auke.model.Tracker", {
	extend : 'Ext.data.Model',
	fields : [ 'id', 'layerId', 'name', 'imei', 'simPhone', 'active', {
		name : 'createDate',
		type : 'date',
		convert : function(value, record) {
			return value ? new Date(value) : '';
		}
	}, {
		name : 'modifiedDate',
		type : 'date',
		convert : function(value, record) {
			return value ? new Date(value) : '';
		}
	} ]

});
