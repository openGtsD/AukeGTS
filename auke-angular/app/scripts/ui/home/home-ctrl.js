'use strict';

angular.module('aukeGTS').constant('HomeCtrl', {
    name: 'HomeCtrl',
    resolve: {}
});

angular.module('aukeGTS').controller('HomeCtrl', ['$scope', '$stateParams', 'trackerService', '$timeout', function ($scope, $stateParams, trackerService, $timeout) {
    $scope.markers = [];

    $scope.layers = trackerService.trackerAPI.getLayers();

    if($stateParams.id != null && $stateParams.id != '') {
        trackerService.trackerAPI.setUUID($stateParams.id);
        $scope.layer = ($stateParams.layer !== '' && $scope.layers[1].value == $stateParams.layer) ? $scope.layers[1] : $scope.layers[0];
    } else {
        //TODO THAI. Need fix when we have more Layer in next time
        $scope.layer = trackerService.trackerAPI.getLayer() == '' ? $scope.layers[0] : trackerService.trackerAPI.getLayer().value == $scope.layers[1].value ? $scope.layers[1] : $scope.layers[0];

    }

    trackerService.trackerAPI.setLayer($scope.layer);

    $scope.showMarkers = function (wait) {
        $timeout(function () {
            $scope.markers = trackerService.trackerAPI.getMarkers();
        }, wait);
    }

    $scope.map = trackerService.map;
    $scope.showMarkers(500);

    $scope.$watch('map', function () {
        $scope.showMarkers(500);
    }, true);

    $scope.update = function () {
        trackerService.trackerAPI.setLayer($scope.layer);
        trackerService.trackerAPI.loadDroneWithinCurrentView();
        $scope.showMarkers(500);
    }
}]);
