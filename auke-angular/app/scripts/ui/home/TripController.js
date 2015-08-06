'use strict';


angular.module('aukeGTS').controller('TripController', ['$scope', '$timeout', 'MapService', 'uiGmapGoogleMapApi', function ($scope, $timeout, MapService, uiGmapGoogleMapApi) {
    var service = MapService.mapAPI;

    $scope.isShowTrip = service.getShowTrip();
    $scope.map2 = {};


    $scope.showTrips = function (value, parameter) {
        service.setShowTrip(value);
        $scope.isShowTrip = service.getShowTrip();

        $scope.map2 = {
            center: {
                latitude: parameter.latitude,
                longitude: parameter.longitude
            },
            zoom: 15,
            options: {
                maxZoom: 20,
                minZoom: 3
            },
            control: {},

            //routes: {
            //    start: [
            //        {name: 'Tokyo Station', latlng: '35.6813177190391,139.76609230041504'},
            //        {name: 'Ootemathi Station', latlng: '35.684228393108306,139.76293802261353'}
            //    ],
            //    ways: [
            //        {name: 'Ootemon', latlng: '35.68567497604782,139.7612428665161'},
            //        {name: 'Nijyubashi', latlng: '35.67947017023017,139.75772380828857'},
            //        {name: 'Tokyo Tower', latlng: '日本, �?�京都港区�?公園４-２-８'},
            //        {name: 'Hama-rikyu Gardens', latlng: '日本, �?�京都中央区浜離宮庭園１-1'}
            //    ],
            //    end: [
            //        {name: 'Tokyo Station', latlng: '35.6813177190391,139.76609230041504'},
            //        {name: 'Ootemathi Station', latlng: '35.684228393108306,139.76293802261353'}
            //    ]
            //},
            //routePoints: {
            //    start: {},
            //    end: {}
            //},
            //routesDisplay: [],
            events: {
                tilesloaded: function (maps, eventName, args) {
                },
                dragend: function (maps, eventName, args) {
                },
                zoom_changed: function (maps, eventName, args) {

                },
                idle: function (maps, eventName, args) {
                }
            }
        };

        //$scope.calcRoute($scope.routePoints);

    }

    //$scope.routePoints.start = $scope.map2.routes.start[0];
    //$scope.routePoints.end = $scope.map2.routes.end[1];
    //
    //var directionsDisplay = new google.maps.DirectionsRenderer();
    //
    //$scope.calcRoute = function (routePoints) {
    //    var ways = [];
    //    for (var i = 0; i < routePoints.ways.length; i++) {
    //      ways.push({
    //        location:routePoints.ways[i].latlng,
    //        stopover:true
    //      });
    //    };
    //    var directionsService = new google.maps.DirectionsService();
    //    directionsDisplay.setMap($scope.map2.control.getGMap());
    //    var start = routePoints.start.latlng;
    //    var end = routePoints.end.latlng;
    //    var request = {
    //      origin: start,
    //      destination: end,
    //      waypoints: ways,
    //      optimizeWaypoints: true,
    //      travelMode: google.maps.TravelMode.WALKING
    //    };
    //    var routesDisplay = [];
    //    directionsService.route(request, function(response, status) {
    //      if (status == google.maps.DirectionsStatus.OK) {
    //        directionsDisplay.setDirections(response);
    //        var route = response.routes[0];
    //        for (var i = 0; i < route.legs.length; i++) {
    //          var routeid = i + 1;
    //          routesDisplay.push({
    //            title: routeid,
    //            start: route.legs[i].start_address,
    //            end: route.legs[i].end_address,
    //            distance: route.legs[i].distance.text
    //          });
    //        }
    //      }
    //    });
    //    $scope.routesDisplay = routesDisplay;
    //    return;
    //  };

}]);
