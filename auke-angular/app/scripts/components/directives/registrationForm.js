/**
 * Created by thaihuynh on 6/27/2015.
 */
'use strict';

angular.module('aukeGTS').directive('registrationForm', function () {
    return {
        templateUrl: function(tElemenet,tAttrs){
            return tAttrs.templateUrl || '/app/scripts/components/directives/registrationForm.tpl.html';
        },
        link: function(scope, element,attrs){
            scope.mode = attrs.mode;

        }
    };
});
