'use strict';

angular.module('aukeGTS').config(function ($stateProvider, $urlRouterProvider, TrackerCtrl) {

    $urlRouterProvider.otherwise('/register');
    $stateProvider
        .state('update', {
            url: '/update',
            templateUrl: 'app/scripts/ui/tracker/update.html',
            controller: TrackerCtrl.name,
            resolve: TrackerCtrl.resolve

        })
        .state('register', {
            url: '/register',
            templateUrl: 'app/scripts/ui/tracker/register.html',
            controller: TrackerCtrl.name,
            resolve: TrackerCtrl.resolve
        })
        .state('delete', {
            url: '/delete',
            templateUrl: 'app/scripts/ui/tracker/delete.html',
            controller: TrackerCtrl.name,
            resolve: TrackerCtrl.resolve
        })

});
