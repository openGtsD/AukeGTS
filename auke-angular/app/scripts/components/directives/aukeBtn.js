'use strict';

angular.module('aukeGTS').directive('aukeBtn', function (aukeUtil) {
    return {
        templateUrl: function(tElemenet,tAttrs){
            var pathToTemplate = aukeUtil.baseURL + '/app/scripts/components/directives/aukeBtn.tpl.html';
            return tAttrs.templateUrl || pathToTemplate;
        },
        link: function(scope, element,attrs){
            scope.hiddenHome = attrs.hiddenHome;

        }
    };
});
