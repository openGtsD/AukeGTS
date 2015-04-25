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
			'trackerForm form button[action=Add], trackerForm form button[action=Delete]' : {
				click : me.updateTracker
			},
//			'trackerForm form button[action=Info]' : {
//				click : me.getTracker
//			}
		});
	},
	
	 allowEnterOnForm : function(form) {
		 var myMode = form.up().mode;
	     this.keyNav = Ext.create('Ext.util.KeyNav', form.el, {
	            enter : function() {
	            	this.updateTracker(form.down('button[action='+myMode+']'));
	            },
	            scope : this
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
				Ext.Msg.confirm("Confirm", "Are you sure delete this tracker. ", function(btn, text) {
			            if (btn != 'yes') {
			                return;
			            } else {
			            	me.remove(iRecord.get('id'));
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
	
	buildURLFromMode : function(mode){
		if(mode === 'Add') {
			return Auke.utils.buildURL('drone/register', true)
		} else if(mode === 'Delete'){
			return Auke.utils.buildURL('drone/remove', true)
		}
	},

	updateTracker : function(button) {
		var me = this;
		var formContainer = button.up('form');
		var mode = formContainer.up().mode;
		var form = formContainer.getForm();
		if (form.isValid()) {
			form.loadRecord(Ext.create('Auke.model.Tracker', form.getValues()));
			var record = form.getRecord();
			var request = me.buildURLFromMode(mode);
			Ext.Ajax.request({
				url : request, 
				method : 'POST',
				jsonData : record.get('id'),
				success : function(response, opts) {
					var res = Ext.JSON.decode(response.responseText);
					if (res.success) {
						Ext.Msg.alert('Success', mode + ' Successfully');
//						if(me.getTrackerGrid()) {
//							var store = me.getTrackerGrid().getStore();
//							if (record.data.id != ""){
//								recToUpdate = store.getById(record.data.id);
//								recToUpdate.set(res.data[0]);
//								recToUpdate.commit();
//								me.getTrackerGrid().getView().refreshNode(store.indexOfId(record.data.id));
//							} else {
//								me.getTrackerGrid().getStore().add(res.data[0]);
//								form.findField('id').setValue(res.data[0].id);
//							}
//						}
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