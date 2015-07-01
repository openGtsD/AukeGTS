'use strict';
var domain = "http://89.221.242.156:8080";
var service = "http://89.221.242.66:8080";
angular.module('aukeGTS', [
    'ngResource',
    'ui.router',
    'ui.bootstrap',
    'uiGmapgoogle-maps'
])
    .factory('aukeUtil', function () {
        return {
            baseURL: domain,
            serviceURL: service
        };
    })
    .config(function ($sceDelegateProvider) {

        $sceDelegateProvider.resourceUrlWhitelist([
            // Allow same origin resource loads.
            'self',
            // Allow loading from our assets domain.  Notice the difference between * and **.
            domain + '/app/scripts/components/directives/**',
            domain + '/app/scripts/ui/tracker/*.html'
        ]);

        //// The blacklist overrides the whitelist so the open redirect here is blocked.
        //$sceDelegateProvider.resourceUrlBlacklist([
        //    'http://localhost:8888/*',
        //])
    });