'use strict';

angular.module('aukeGTS').factory('Tracker', function ($resource) {
  var serviceURL = 'localhost:8080';
  var baseUrl = serviceURL + '/drone/';

  return $resource(baseUrl, {}, {
    getTracker: {
      url   : baseUrl + ':trackerId',
      method: 'GET'
    },

    loadDroneWithinView  : {
      url   : baseUrl + 'load-drone-in-view/' + ':layerId' + ':zoom',
      method: 'POST'
    }
  });
});
