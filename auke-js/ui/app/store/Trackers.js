Ext.define("Auke.store.Trackers", {
    extend : 'Ext.data.Store',
    model : 'Auke.model.Tracker',
    proxy : {
        type : 'rest',
        headers : {
            'Content-Type' : 'application/json; charset=utf-8'
        },
        url : Auke.utils.buildURL('drone/get-all/SIMULATED', true),
        reader : {
            type : 'json',
            root : 'data',
            successProperty : 'success'
        },
        writer : {
            type : 'json',
            writeAllFields : true
        }
    }
});
