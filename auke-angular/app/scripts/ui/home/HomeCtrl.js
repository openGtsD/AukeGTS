'use strict';


angular.module('aukeGTS').controller('HomeCtrl', ['$scope', '$stateParams', '$timeout', '$interval', 'MapService', function ($scope, $stateParams, $timeout, $interval, MapService) {
    var service = MapService.mapAPI;
    $scope.markers = [];
    $scope.layers = service.getLayers();

    if($stateParams.id != null && $stateParams.id != '') {
        service.setUUID($stateParams.id);
        $scope.layer = ($stateParams.layer !== '' && $scope.layers[1].value == $stateParams.layer) ? $scope.layers[1] : $scope.layers[0];
    } else {
        //TODO THAI. Need fix when we have more Layer in next time
        $scope.layer = service.getLayer() == '' ? $scope.layers[0] : service.getLayer().value == $scope.layers[1].value ? $scope.layers[1] : $scope.layers[0];
    }

    service.setLayer($scope.layer);

    $scope.showMarkers = function (wait) {
        $timeout(function () {
            $scope.markers = service.getMarkers();
        }, wait);
    }

    $scope.map = MapService.map;
    $scope.showMarkers(1000);

    var promise = undefined;
    $scope.stop = function() {
        $interval.cancel(promise);
    };

    $scope.start = function() {
        $scope.stop();
        promise = $interval(function () {
            service.loadDroneWithinCurrentView();
        }, 10000);
    }

    $scope.$watch('map', function () {
        $scope.showMarkers(200);

        if($scope.map.zoom >= 11) {
            $scope.start();
        } else {
            $scope.stop();
        }

    }, true);

    $scope.update = function () {
        service.setLayer($scope.layer);
        service.loadDroneWithinCurrentView();
        $scope.showMarkers(500);
    }

    $scope.$on('$destroy', function() {
        $scope.stop();
    });

}]);
