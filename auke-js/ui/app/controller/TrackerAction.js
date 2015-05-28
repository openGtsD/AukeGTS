Ext.define('Auke.controller.TrackerAction', {
	extend : 'Ext.app.Controller',

	stores : [ 'Trackers', 'Feeds'],
	models : [ 'Tracker', 'Feed'],

	refs : [ {
		ref : 'trackerGrid',
		selector : 'trackerGrid'
	}, {
		ref : 'trackerForm',
		selector : 'trackerForm form'
	},  {
		ref : 'feedGrid',
		selector : 'feedGrid'
	}, {
        ref : 'home',
        selector : 'home'
    }],
	init : function() {
		var me = this;
		me.control({
			'trackerForm form' : {
				afterrender : me.allowEnterOnForm
			},
			'trackerGrid' : {
				afterrender : me.loadAll,
				cellclick : me.editOrDelete
			},
			'trackerForm form button[action=clearBtn], loginForm form button[action=clearBtn]' : {
				click : me.clear
			},
			'trackerForm form button[action=Add], trackerForm form button[action=Delete], trackerForm form button[action=Info]' : {
				click : me.saveTracker
			},
			'trackerForm form button[action=Update]' : {
				click:  me.updateTracker
			},
			'manageTracker combo' : {
	            select : me.loadAllTracker
	        },
	        'textareafield' : {
				afterrender : me.loadAllFeed
			},
			'gmappanel' : {
				mapready: me.initMarkerManager,
				idle: me.getTrackers,
				zoom_changed: me.startThread
			},
			'home combo' : {
	            select : me.loadTrackerFromLayerId
	        },
		});
	},
	
	startThread: function(gmappanel) {
		var me = this;
		var layer = me.getHome().getLayer();
		var interval = setInterval(function() {
			var zoomLevel = gmappanel.getMap().getZoom();
			if(me.getHome()) {
				layer = me.getHome().getLayer();
				me.loadTrackers(layer);
				if(zoomLevel < 11) {
					clearInterval(interval);
					interval = null;
				}
			}
		}, 10000);
	},
	
	getTrackers :  function(gmappanel) {
		if(this.getHome()) {
			this.loadTrackers(this.getHome().getLayer());
		}
	},
	
	loadTrackerFromLayerId : function(combo, records, eOpts ){
		Auke.utils.reMakeInfoBubble();
		this.getHome().setLayer(combo.getValue());
		this.loadTrackers(combo.getValue())
	},
	
	initMarkerManager : function(gmappanel) {
		if(gmappanel) {
			Auke.utils.mgr = new MarkerManager(gmappanel.getMap());
			
		}
	},
	
	loadTrackers : function(layerId){
		var me = this;
		if(!Auke.utils.isSending) {
			Auke.utils.isSending = true;
			var map = Auke.utils.mgr.map_;
			if(!map) {
				return;
			} 
			if(map.getZoom() < 11) {
				Auke.utils.reMakeInfoBubble();
			}
			var mapBound = map.getBounds();
			var ne = mapBound.getNorthEast(); // LatLng of the north-east corner
			var sw = mapBound.getSouthWest();
			Ext.Ajax.request({
				url : Auke.utils.buildURL('drone/load-drone-in-view/', true) + layerId + "/" + map.getZoom(),
				jsonData : JSON.stringify({
					southWestLat : sw.lat(),
					southWestLon : sw.lng(),
					northEastLat : ne.lat(),
					northEastLon : ne.lng()
				}),
				method : 'POST',
				success : function(response) {
					var res = Ext.JSON.decode(response.responseText);
					var data = res.data;
					Auke.utils.mgr.clearMarkers();
					var markers = [];
					for (var i = 0; i < data.length; i++) {
						var posn = new google.maps.LatLng(data[i].currentPosition.latitude,
								data[i].currentPosition.longitude);
						var marker = Auke.utils.createMarker(data[i].id, posn, data[i].name, data[i].numtrackers, layerId, map);
						markers.push(marker);
					}
					Auke.utils.mgr.addMarkers(markers,3);
					Auke.utils.mgr.refresh();
					
					Ext.fly("zoomId").update(map.getZoom())
					Ext.fly("type").update(map.getZoom() >= 11 ? "drone" : "position");
					Ext.fly("trackerNumber").update(Auke.utils.mgr.getMarkerCount(map.getZoom()));
					Auke.utils.isSending = false;
				}
			});
		}
	},
	
	allowEnterOnForm : function(form) {
		 var myMode = form.up().mode;
	     this.keyNav = Ext.create('Ext.util.KeyNav', form.el, {
	            enter : function() {
	            	this.saveTracker(form.down('button[action='+myMode+']'));
	            },
	            scope : this
	        });
	 },
	 loadAllTracker : function(combo, records, eOpts ){
		 this.getTrackerGrid().getStore().proxy.url =  Auke.utils.buildURL('drone/get-all/', true) + combo.getValue(),
			this.getTrackerGrid().getStore().loadData([], false);
			this.getStore('Trackers').load({
				scope : this,
				callback : function(records, operation, success) {
				}
			});
	 },
	 
	 loadAll : function(grid) {
		this.getTrackerGrid().getStore().proxy.url =  Auke.utils.buildURL('drone/get-all/REAL', true),
		this.getTrackerGrid().getStore().loadData([], false);
		this.getStore('Trackers').load({
			scope : this,
			callback : function(records, operation, success) {
			}
		});
	},
	
	loadAllFeed : function(text) {
		Ext.Ajax.request({
			url : Auke.utils.buildURL('rss/SIMULATED/registered', true), 
			method : 'GET',
			success : function(response, opts) {
				var res = response.responseText
				text.setRawValue(res);
			},
			failure : function(response) {
				var res = response.responseText;
				Ext.Msg.alert('Errors', res);
			}
		})
	},
	
	openNewWindow : function(iView, iCellEl, iColIdx, iRecord, iRowEl, iRowIdx,
			iEvent) {
		 var fieldName = iView.getGridColumns()[iColIdx].dataIndex;
	        var linkClicked = (iEvent.target.tagName.toUpperCase() == 'SPAN');
	        if (linkClicked) {
	        	window.open(iRecord.get('url'));
	        }
	},

	editOrDelete : function(iView, iCellEl, iColIdx, iRecord, iRowEl, iRowIdx,
			iEvent) {
		var me = this;
		var fieldName = iView.getGridColumns()[iColIdx].dataIndex;

		var actionType = iEvent.getTarget().innerText
				|| iEvent.getTarget().textContent;
		var linkClicked = (iEvent.target.tagName.toUpperCase() == 'BUTTON');
		var action = actionType == 'Edit' ? actionType : 'Delete';
		if (linkClicked && fieldName == "actionColumn" && iRecord != null) {
			if (action == 'Delete') {
				Ext.Msg.confirm("Confirm", "Are you sure delete this tracker. ", function(btn, text) {
			            if (btn != 'yes') {
			                return;
			            } else {
			            	me.removeTracker(iRecord);
			            }
			    })
			} else if(action == 'Edit'){
				me.getTrackerForm().loadRecord(iRecord);
			}
		}
	},
	
	clear : function(button) {
		var me = this;
		var formContainer = button.up('form');
		var form = formContainer.getForm();
		form.reset();
	},
	
	buildURLFromMode : function(mode, id){
		if(mode === 'Add') {
			return Auke.utils.buildURL('drone/register/', true) + id
		} else if(mode === 'Delete'){
			return Auke.utils.buildURL('drone/remove/', true) + id
		} else if(mode == 'Info'){
			return Auke.utils.buildURL('drone/get-tracker/', true) + id
		}
	},
	removeTracker : function(tracker) {
		var me = this;
		Ext.Ajax.request({
			url : Auke.utils.buildURL('drone/remove/', true) + tracker.get('id'), 
			method : 'POST',
			success : function(response, opts) {
				var res = Ext.JSON.decode(response.responseText);
				if (res.success) {
					Ext.Msg.alert('Success', 'Delete Successfully');
					if(me.getTrackerGrid()) {
						me.getTrackerGrid().getStore().remove(tracker);
					}
				} else { // get tracker no exist
					Ext.Msg.alert('Information', 'Tracker not exists, please try again!');
				}
			},
			failure : function(response) {
				var res = response.responseText;
				Ext.Msg.alert('Errors', res);
			}
		})
	},
	
	updateTracker : function(button){
		var me = this;
		var formContainer = button.up('form');
		var form = formContainer.getForm();
		if (form.isValid()) {
			form.loadRecord(Ext.create('Auke.model.Tracker', form.getValues()));
			var record = form.getRecord();
			Ext.Ajax.request({
				url : Auke.utils.buildURL('drone/update', true), 
				method : 'POST',
				jsonData : record.data,
				success : function(response, opts) {
					var res = Ext.JSON.decode(response.responseText);
					if (res.success) {
						Ext.Msg.alert('Success', 'Update Successfully');
					} else { // get tracker no exist
						Ext.Msg.alert('Information', 'Tracker not exist, please try again!');
					}
				},
				failure : function(response) {
					var res = response.responseText;
					Ext.Msg.alert('Errors', res);
				}
			})
		}
	},
	
	saveTracker : function(button) {
		var me = this;
		var formContainer = button.up('form');
		var mode = formContainer.up().mode;
		var form = formContainer.getForm();
		if (form.isValid()) {
			form.loadRecord(Ext.create('Auke.model.Tracker', form.getValues()));
			var record = form.getRecord();
			var request = me.buildURLFromMode(mode, record.get('id'));
			Ext.Ajax.request({
				url : request, 
				method : 'POST',
				success : function(response, opts) {
					var res = Ext.JSON.decode(response.responseText);
					if (res.success) {
						if(mode === 'Info') {
							form.getFields().each(function(e) {
								e.show();
							})
							button.setText('Update');
							button.action = 'Update';
							form.loadRecord(Ext.create('Auke.model.Tracker', res.data[0]));
						} else {
							Ext.Msg.alert('Success', mode + ' Successfully');
						}
					} else { // get tracker no exist
						Ext.Msg.alert('Information', 'Tracker not exist, please try again!');
					}
				},
				failure : function(response) {
					var res = response.responseText;
					Ext.Msg.alert('Errors', res);
				}
			})
		}
	}	
})