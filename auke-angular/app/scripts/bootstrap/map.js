'use strict';

angular.module('aukeGTS').config(function ($stateProvider, $urlRouterProvider, HomeCtrl) {

    //Devs
    var domain = "http://89.221.242.156:8080";

    //var domain = "http://89.221.242.156:8080";
    $urlRouterProvider.otherwise('/home');
    $stateProvider
        .state('home', {
            url: '/home',
            templateUrl: domain + '/app/scripts/ui/home/home.html',
            controller: HomeCtrl.name,
            resolve: HomeCtrl.resolve
        })
        .state('viewTracker', {
            url: '/home:{id}:{layer}',
            templateUrl: domain + '/app/scripts/ui/home/home.html',
            controller: HomeCtrl.name,
            resolve: HomeCtrl.resolve
        })
});
