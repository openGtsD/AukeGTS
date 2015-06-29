'use strict';

angular.module('aukeGTS').config(function ($stateProvider, $urlRouterProvider, HomeCtrl) {
    $urlRouterProvider.otherwise('/home');
    $stateProvider
        .state('home', {
            url: '/home',
            templateUrl: 'app/scripts/ui/home/home.html',
            controller: HomeCtrl.name,
            resolve: HomeCtrl.resolve
        })
});
