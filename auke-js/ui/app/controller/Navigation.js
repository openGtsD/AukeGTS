Ext.define('Auke.controller.Navigation', {
	extend : 'Ext.app.Controller',
	refs : [ {
		ref : 'viewport',
		selector : '#viewport'
	} ],
	init : function() {
		this.control({
			'container #pageHeader button' : {
				click : function(button) {
					if (button.view == 'global.Home') {
						Auke.utils.loadView(button.view, null);
					} else if (button.view == 'global.Login') {
						Auke.utils.loadView(button.view, null);
					} else if (button.view == 'global.Register') {
						Auke.utils.loadView(button.view, null);
					} 
				}
			},

			'#viewContainer' : {
				afterrender : function() {
					Ext.History.on('change', function(token) {
						hashString = token ? token : 'global.Home';
						Auke.utils.loadViewFromHash(hashString);
					});

					setTimeout(function() {
						hashString = location.hash ? location.hash.substring(1)
								: 'global.Home';
						Auke.utils.loadViewFromHash(hashString);
					}, 0);
				}
			}
		});
	}

});
