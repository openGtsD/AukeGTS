'use strict';

angular.module('aukeGTS').factory('TrackerService', function ($http, aukeUtil) {
    var serviceURL = aukeUtil.serviceURL;
    var trackerAPI = {};

    trackerAPI.loadDroneWithinView = function (json, layerId, zoom) {
        var url = serviceURL + '/drone/load-drone-in-view/' + layerId + '/' + zoom;
        return $http.post(url, json);
    }
    trackerAPI.create = function (tracker) {
        var url = serviceURL + '/drone/register/';
        return $http.post(url, tracker);
    }
    trackerAPI.delete = function (id) {
        var url = serviceURL + '/drone/delete/' + id;
        return $http.post(url);
    }
    trackerAPI.load = function (id) {
        var url = serviceURL + '/drone/get-tracker/' + id;
        return $http.post(url);
    }
    trackerAPI.update = function (tracker) {
        var url = serviceURL + '/drone/update';
        return $http.post(url, tracker);
    }

    trackerAPI.getTripByTrackerId = function(id) {
        var url = serviceURL + '/drone/get-trips/' + id;
        return $http.post(url);

    }

    trackerAPI.getTripById = function(id) {
        var url = serviceURL + '/drone/get-trip/' + id;
        return $http.post(url);

    }

    trackerAPI.getBaseURL = function() {
        return aukeUtil.baseURL;
    }

    trackerAPI.getAgentTypes = function () {
        //implement later
    }

    return trackerAPI;
});




