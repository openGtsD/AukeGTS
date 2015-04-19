Ext.define('Auke.controller.TrackerAction', {
	extend : 'Ext.app.Controller',

	stores : [ 'Trackers' ],
	models : [ 'Tracker' ],

	refs : [ {
		ref : 'trackerGrid',
		selector : 'trackerGrid'
	}, {
		ref : 'trackerForm',
		selector : 'trackerForm form'
	} ],
	init : function() {
		var me = this;
		me.control({
			'trackerGrid' : {
				afterrender : me.loadAll,
				cellclick : me.editOrDelete
			},
			'trackerForm form button[action=clearBtn]' : {
				click : me.clear
			},
			'trackerForm form button[action=saveBtn]' : {
				click : me.save
			},
			'gmappanel' : {
				mapready : me.loadTracks
			}	
		});
	},
	loadTracks : function(gm, map){
		var mapBound = map.getBounds();
		var ne = mapBound.getNorthEast(); // LatLng of the north-east corner
		var sw = mapBound.getSouthWest();
		Ext.Ajax.request({
			url : 'service/drone/load-drone-in-view/SIMULATED/'
					+ map.getZoom(),
			jsonData : JSON.stringify({
				southWestLat : sw.lat(),
				southWestLon : sw.lng(),
				northEastLat : ne.lat(),
				northEastLon : ne.lng()
			}),
			method : 'POST',
			success : function(response) {
				var res = Ext.JSON.decode(response.responseText);
//				mgr.clearMarkers();
//				var markers = [];
//				for (var i = 0; i < data.length; i++) {
//					posn = new google.maps.LatLng(data[i].currentPosition.latitude,
//							data[i].currentPosition.longitude);
//					var marker = createMarker(data[i].id, posn, data[i].name,
//							'/auke-js/resources/images/drone.png',
//							buildHTML(data[i]));
//					markers.push(marker);
//					allmarkers[data[i].id] = marker;
//				}
//				// mgr.addMarkers(markers, data[i].minZoom, data[i].maxZoom); TODO:
//				// its use full for show drones in zoom factory
//				mgr.addMarkers(markers, 3, 19);
//				mgr.refresh();
//				updateStatus(mgr.getMarkerCount(map.getZoom()));
//				// autoCenter(markers);
			}
		});
	},
	
	loadAll : function(grid) {
		this.getTrackerGrid().getStore().loadData([], false);
		this.getStore('Trackers').load({
			scope : this,
			callback : function(records, operation, success) {
			}
		});
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
				alert("Comming soon")
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

	save : function(button) {
		var me = this;
		var formContainer = button.up('form');
		var form = formContainer.getForm();
		if (form.isValid()) {
			form.loadRecord(Ext.create('Auke.model.Tracker', form.getValues()));
			var record = form.getRecord();
			Ext.Ajax.request({
				url : 'service/drone/update',
				method : 'POST',
				jsonData : record.data,
				success : function(response, opts) {
					var res = Ext.JSON.decode(response.responseText);
					if (res.success) {
						me.getTrackerGrid().getStore().add(record)
					}
				}
			})

		}
	}

})