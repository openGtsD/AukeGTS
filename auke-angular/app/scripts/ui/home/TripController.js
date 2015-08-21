'use strict';


angular.module('aukeGTS').controller('TripController', ['$scope', '$stateParams', '$timeout', 'MapService', 'uiGmapGoogleMapApi', function ($scope, $stateParams, $timeout, MapService, uiGmapGoogleMapApi) {
    var service = MapService.mapAPI;

    $scope.map2 = {
        center: {
            latitude: 0,
            longitude: 0
        },
        zoom: 3,
        options: {
            maxZoom: 21,
            minZoom: 3
        },
        control: {},
        events: {
            tilesloaded: function (maps, eventName, args) {
                $scope.map =  maps;
            },
            dragend: function (maps, eventName, args) {
            },
            zoom_changed: function (maps, eventName, args) {
            },
            idle: function (maps, eventName, args) {
                $scope.map =  maps;
            }
        }
    };

    $scope.markers = [];
    $scope.trips = service.getTrips();
    $scope.tripInfo = service.getTripInfo();
    if ($stateParams.trackerId != null && $stateParams.trackerId != '') {
        service.getTripByTrackerId($stateParams.trackerId);
        $scope.$watch('trips', function () {
            $timeout(function () {
                $scope.trips = service.getTrips();
            }, 500);
        });
    }

    if($stateParams.tripId != null && $stateParams.tripId != '') {
        service.getTripById($stateParams.tripId);
        $scope.$watch('tripInfo', function () {
            $timeout(function () {
                $scope.tripInfo = service.getTripInfo();

                var markers = [];
                if($scope.tripInfo.route && $scope.tripInfo.route.length > 0) {
                    var start = $scope.tripInfo.route[0];
                    markers.push(service.createTripIcon(start.latitude, start.longitude, 0));

                    var end = $scope.tripInfo.route[$scope.tripInfo.route.length - 1];
                    markers.push(service.createTripIcon(end.latitude, end.longitude, -1));

                    for(var i = 1; i < $scope.tripInfo.route.length - 1; i++) {
                        var point = $scope.tripInfo.route[i];
                        markers.push(service.createTripIcon(point.latitude, point.longitude, i));
                    }
                    $scope.markers = markers;
                }
            }, 500);
        });
    }

}])
