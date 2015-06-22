angular.module('aukeGTS').factory('trackerService', function($http) {
    var serviceURL = 'http://localhost:8080/';
    var trackerAPI = {};

    trackerAPI.loadDroneWithinView = function(json, layerId, zoom) {
      var url = serviceURL + 'drone/load-drone-in-view/' + layerId + '/' + zoom;
      return $http.post(url, json);
    }
    return trackerAPI;
  });