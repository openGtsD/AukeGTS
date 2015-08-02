'use strict';

angular.module('aukeGTS').constant('HomeCtrl', {
    name: 'HomeCtrl',
    resolve: {}
});

angular.module('aukeGTS').controller('HomeCtrl', ['$scope', '$stateParams', 'trackerService', '$timeout', function ($scope, $stateParams, trackerService, $timeout) {
    $scope.markers = [];

    trackerService.trackerAPI.setLayer($stateParams.layer);
    trackerService.trackerAPI.setUUID($stateParams.id);


    $scope.showMarkers = function(wait) {
        $timeout(function () {
            $scope.markers = trackerService.trackerAPI.getMarkers();
        }, wait);
    }

    $scope.map = trackerService.map;
    $scope.showMarkers(500);

    $scope.$watch('map', function () {
        $scope.showMarkers(500);
    }, true);

    $scope.update = function (map) {
        trackerService.trackerAPI.setLayer(map.layer);
        trackerService.trackerAPI.loadDroneWithinCurrentView();
        $scope.showMarkers(500);
    }
}]);
