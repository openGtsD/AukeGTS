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
    .state('update', {
        url: '/update',
        templateUrl: 'app/scripts/ui/tracker/update.html'
    })
    .state('register', {
        url: '/register',
        templateUrl: 'app/scripts/ui/tracker/register.html'
     })
     .state('delete', {
        url: '/delete',
        templateUrl: 'app/scripts/ui/tracker/delete.html'
     })
});
