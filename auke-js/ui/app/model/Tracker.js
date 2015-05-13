Ext.define("Auke.model.Tracker", {
	extend : 'Ext.data.Model',
	fields : [ 'id', 'name', 'simPhone', 'active', 'lon', 'lat', 'altitude', {
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
