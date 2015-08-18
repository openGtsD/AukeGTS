'use strict';


angular.module('aukeGTS').controller('TripController', ['$scope', '$stateParams', '$timeout', 'MapService', 'uiGmapGoogleMapApi', function ($scope, $stateParams, $timeout, MapService, uiGmapGoogleMapApi) {
    var service = MapService.mapAPI;

    $scope.map2 = {};
    $scope.m = null;

    if ($stateParams.id != null && $stateParams.id != '') {
        service.setTripId($stateParams.id);
        $scope.map2 = {
            center: {
                latitude: 0,
                longitude: 0
            },
            zoom: 8,
            options: {
                maxZoom: 21,
                minZoom: 3
            },
            control: {},
            events: {
                tilesloaded: function (maps, eventName, args) {
                    $scope.m = maps;
                },
                dragend: function (maps, eventName, args) {
                },
                zoom_changed: function (maps, eventName, args) {

                },
                idle: function (maps, eventName, args) {
                    $scope.m = maps;
                }
            }
        };

        var directionsDisplay = new google.maps.DirectionsRenderer();
        $scope.calcRoute = function () {console.log(service.getRoute());
            console.log(service.getStartPoint());
            console.log(service.getEndPoint());
            var ways = [];
            for (var i = 0; i < service.getRoute().length; i++) {
                if(i <= 6) {//TODO: limit wayspoint from google API
                    ways.push({
                        location: service.getRoute()[i].latitude + ',' + service.getRoute()[i].longitude,
                        stopover: true
                    });
                }
            }

            directionsDisplay.setMap($scope.m);
            var directionsService = new google.maps.DirectionsService();

            var request = {
                origin: service.getStartPoint(),
                destination: service.getEndPoint(),
                waypoints: ways,
                optimizeWaypoints: true,
                travelMode: google.maps.TravelMode.WALKING
            };
            directionsService.route(request, function (response, status) {console.log(status);
                if (status == google.maps.DirectionsStatus.OK) {
                    directionsDisplay.setDirections(response);
                }
            });
            return;
        }
        //if ($scope.isShowTrip) {
        $timeout(function () {
            $scope.calcRoute();
        }, 1000);
        //}
    }
}])
