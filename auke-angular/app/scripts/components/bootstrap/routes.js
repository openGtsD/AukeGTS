'use strict';

angular.module('aukeGTS').config(function ($stateProvider, $urlRouterProvider,  HomeCtrl, TrackerCtrl) {
    var domain = "http://89.221.242.156:8080";
    $urlRouterProvider.otherwise('/home');
    $stateProvider
        .state('home', {
            url: '/home',
            templateUrl: domain + '/app/scripts/ui/home/home.html',
            controller: HomeCtrl.name,
            resolve: HomeCtrl.resolve
        })
        .state('update', {
            url: '/update',
            templateUrl: domain + '/app/scripts/ui/tracker/update.html',
            controller: TrackerCtrl.name,
            resolve: TrackerCtrl.resolve
        })
        .state('register', {
            url: '/register',
            templateUrl: domain + '/app/scripts/ui/tracker/register.html',
            controller: TrackerCtrl.name,
            resolve: TrackerCtrl.resolve
        })
        .state('delete', {
            url: '/delete',
            templateUrl:  domain + '/app/scripts/ui/tracker/delete.html',
            controller: TrackerCtrl.name,
            resolve: TrackerCtrl.resolve
        })
});
