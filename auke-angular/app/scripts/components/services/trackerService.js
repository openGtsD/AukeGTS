angular.module('aukeGTS').factory('trackerService', function($http, aukeUtil) {
    var serviceURL = aukeUtil.serviceURL;
    var trackerAPI = {};

    trackerAPI.loadDroneWithinView = function(json, layerId, zoom) {
      var url = serviceURL + '/drone/load-drone-in-view/' + layerId + '/' + zoom;
      return $http.post(url, json);
    }

    trackerAPI.getTracker = function(marker, map) {
        return trackerAPI.load(marker.key);
    }

    trackerAPI.create = function(tracker) {
        var url = serviceURL + '/drone/registertk/';
        return $http.post(url, tracker);
    }

    trackerAPI.delete = function(id) {
        var url = serviceURL + '/drone/remove/' + id;
        return $http.post(url);
    }

    trackerAPI.load = function(id) {
        var url = serviceURL + '/drone/get-tracker/' + id;
        return $http.post(url);
    }

    trackerAPI.update = function(tracker) {
        var url = serviceURL + '/drone/update';
        return $http.post(url, tracker);
    }

    return trackerAPI;
});



