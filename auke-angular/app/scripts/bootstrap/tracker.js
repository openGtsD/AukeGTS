'use strict';

angular.module('aukeGTS').config(function ($stateProvider, $urlRouterProvider, TrackerCtrl) {

    //Devs
    var domain = "http://89.221.242.156:8080";

    //var domain = "http://localhost:8080";
    $urlRouterProvider.otherwise('/register');
    $stateProvider
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
            templateUrl: domain + '/app/scripts/ui/tracker/delete.html',
            controller: TrackerCtrl.name,
            resolve: TrackerCtrl.resolve
        })

});
