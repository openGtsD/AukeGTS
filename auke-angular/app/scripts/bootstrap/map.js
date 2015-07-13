'use strict';

angular.module('aukeGTS').config(function ($stateProvider, $urlRouterProvider, HomeCtrl) {

    //Prod
    var domain = "http://89.221.242.66:8888";

    //Devs
    //var domain = "http://89.221.242.156:8888";

    //var domain = "http://89.221.242.156:8888";
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
