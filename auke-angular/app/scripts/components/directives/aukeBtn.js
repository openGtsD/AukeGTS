'use strict';

angular.module('aukeGTS').directive('aukeBtn', function () {

    return {
        templateUrl: function(tElemenet,tAttrs){
            return tAttrs.templateUrl || '/app/scripts/components/directives/aukeBtn.tpl.html';
        },
        link: function(scope, element,attrs){
            scope.hiddenHome = attrs.hiddenHome;

        }
    };
});
