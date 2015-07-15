/**
 * Created by thaihuynh on 6/27/2015.
 */
'use strict';

angular.module('aukeGTS').directive('registrationForm', function (aukeUtil) {
    return {
        templateUrl: function(tElemenet,tAttrs){
            return tAttrs.templateUrl || aukeUtil.baseURL + '/app/scripts/components/directives/registrationForm.tpl.html';
        },
        link: function(scope, element,attrs){
            scope.mode = attrs.mode;

        }
    };
});
