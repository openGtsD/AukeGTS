'use strict';

angular.module('aukeGTS').config(function ($stateProvider, $urlRouterProvider) {

    //Devs
    var domain = "http://89.221.242.156:8080";

    //var domain = "http://localhost:8081";
    $urlRouterProvider.otherwise('/home');
    $stateProvider
        .state('home', {
            url: '/home',
            templateUrl: domain + '/app/scripts/ui/home/home.html',
            controller: 'HomeCtrl'

        })
        .state('viewTracker', {
            url: '/home:{id}:{layer}',
            templateUrl: domain + '/app/scripts/ui/home/home.html',
            controller: 'HomeCtrl'
        })
        .state('viewTrip', {
            url: '/home:{id}',
            templateUrl: domain + '/app/scripts/ui/home/trip.html',
            controller: 'TripController'
        })
});
