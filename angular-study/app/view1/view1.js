'use strict';

angular.module('myApp.view1', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/view1', {
    templateUrl: 'view1/partials/drivers.html',
    controller: 'View1Ctrl'
  }).when('/view1/:id', {
        templateUrl: 'view1/partials/driver.html',
        controller: 'View1Ctrl'
  }).otherwise({redirectTo: '/view1'});

}])

.controller('View1Ctrl',  function($scope, ergastAPIservice) {
      $scope.nameFilter = null;
      $scope.driversList = [];
      $scope.searchFilter = function (driver) {
        var re = new RegExp($scope.nameFilter, 'i');
        return !$scope.nameFilter || re.test(driver.Driver.givenName) || re.test(driver.Driver.familyName);
      };

      ergastAPIservice.getDrivers().success(function (response) {
        //Digging into the response to get the relevant data
        $scope.driversList = response.MRData.StandingsTable.StandingsLists[0].DriverStandings;
      });
    }
).
controller('driverController', function($scope, $routeParams, ergastAPIservice) {
      $scope.id = $routeParams.id;
      $scope.races = [];
      $scope.driver = null;

      ergastAPIservice.getDriverDetails($scope.id).success(function (response) {
        $scope.driver = response.MRData.StandingsTable.StandingsLists[0].DriverStandings[0];
      });

      ergastAPIservice.getDriverRaces($scope.id).success(function (response) {
        $scope.races = response.MRData.RaceTable.Races;
      });
})

.factory('ergastAPIservice', function($http) {

      var ergastAPI = {};

      ergastAPI.getDrivers = function() {
        return $http({
          method: 'JSONP',
          url: 'http://ergast.com/api/f1/2013/driverStandings.json?callback=JSON_CALLBACK'
        });
      }

      ergastAPI.getDriverDetails = function(id) {
        return $http({
          method: 'JSONP',
          url: 'http://ergast.com/api/f1/2013/drivers/'+ id +'/driverStandings.json?callback=JSON_CALLBACK'
        });
      }

      ergastAPI.getDriverRaces = function(id) {
        return $http({
          method: 'JSONP',
          url: 'http://ergast.com/api/f1/2013/drivers/'+ id +'/results.json?callback=JSON_CALLBACK'
        });
      }

      return ergastAPI;
    }
)

