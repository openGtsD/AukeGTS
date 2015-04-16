Ext.Loader.setConfig({
	enabled : true
});

Ext.application({
	name : 'Auke',
	requires : [ 'Auke.view.Footer', ],
	controllers : [ 'ViewPortController' ],
	appFolder : '/auke-js/ui/app',
	autoCreateViewport : true,
	launch : function() {
	}
})
