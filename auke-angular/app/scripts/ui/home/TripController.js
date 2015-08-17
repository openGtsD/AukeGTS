'use strict';


angular.module('aukeGTS').controller('TripController', ['$scope', '$timeout', 'MapService', 'uiGmapGoogleMapApi', function ($scope, $timeout, MapService, uiGmapGoogleMapApi) {
    var service = MapService.mapAPI;
    $scope.isShowTrip = service.getShowTrip();
    $scope.map2 = {};
    $scope.m = null;

    $scope.showTrips = function (value, parameter) {

        service.setShowTrip(value, parameter.id);
        $scope.isShowTrip = service.getShowTrip();

        $scope.map2 = {
            center: {
                latitude: parameter.latitude,
                longitude: parameter.longitude
            },
            zoom: 8,
            options: {
                maxZoom: 20,
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
        $scope.calcRoute = function () {
            var ways = [];
            for (var i = 0; i < service.getRoute().length; i++) {
                ways.push({
                    location: service.getRoute()[i].latitude+','+service.getRoute()[i].longitude,
                    stopover: true
                });
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
            directionsService.route(request, function (response, status) {
                if (status == google.maps.DirectionsStatus.OK) {
                    directionsDisplay.setDirections(response);
                }
            });
            return;
        }
        if ($scope.isShowTrip) {
            $timeout(function () {
                $scope.calcRoute();
            }, 1000);
        }
    }
}]);
