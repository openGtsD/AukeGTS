Ext.Loader.setConfig({
	enabled : true
});

Ext.application({
	name : 'Auke',
	requires : [ 'Ext.form.*', 'Ext.tab.Bar', 'Ext.grid.column.Action',
			'Ext.History', 'Ext.layout.component.field.Field',
			'Ext.toolbar.Paging', 'Auke.view.Footer', 'Auke.view.global.Home',
			'Auke.view.grid.TrackerGrid', 'Auke.view.global.TrackerForm', 'Auke.view.global.Register', 'Auke.view.global.Login' ],
	controllers : [ 'Navigation', 'TrackerAction' ],
	appFolder : Auke.utils.buildURL('auke-js/ui/app'),
	autoCreateViewport : true,
	launch : function() {
		Ext.History.init();
	}
})
