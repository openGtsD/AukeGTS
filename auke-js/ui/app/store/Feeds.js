Ext.define("Auke.store.Feeds", {
    extend : 'Ext.data.Store',
    model : 'Auke.model.Feed',
    proxy : {
        type : 'rest',
        headers : {
            'Content-Type' : 'application/json; charset=utf-8'
        },
        url : Auke.utils.buildURL('rss/SIMULATED/registered', true),
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
