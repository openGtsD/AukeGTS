/**
 * Created by thaihuynh on 6/25/2015.
 */
'use strict';

angular.module('aukeGTS').constant('TrackerCtrl', {
    name: 'TrackerCtrl',
    resolve: {}
});

angular.module('aukeGTS').controller('TrackerCtrl', ['$scope', 'trackerService', function ($scope, trackerService) {

    $scope.formModel = (typeof $scope.formModel==='object') ? $scope.formModel : {
        id: '',
        name: '',
        simPhone: ''
    };
    $scope.update = function(mode){
        $scope.error = "";
        $scope.proccessing = true;
        if($scope.formModel.id == ""){
            $scope.success = false;
            $scope.error = "This field is required";
            $scope.proccessing = false;
            return;
        }

        if(mode === "register") {
            trackerService.create($scope.formModel.id).success(function (response) {
                $scope.success = response.success;
                if(response.success) {
                    $scope.msg = 'Your tracker has been created successfully.';
                } else {
                    $scope.error = 'Please check make sure your services is running.';
                }
                $scope.proccessing = false;
            });
        } else if(mode === "delete"){
            trackerService.delete($scope.formModel.id).success(function (response) {
                $scope.success = response.success;
                if (response.success) {
                    $scope.msg = 'Your tracker has delete successfully';
                } else {
                    $scope.error = 'Tracker not exists, please try again!';
                }
                $scope.proccessing = false;

            });
        } else if(mode == "load"){
            trackerService.load($scope.formModel.id).success(function (response) {
                $scope.success = false;
                if (response.success) {
                   $scope.isDisabled = true;
                   $scope.isUpdate = true;
                   var data = response.data;
                   $scope.mode = "update";
                   $scope.formModel = data[0];
                } else {
                    $scope.error = 'Tracker not exists, please try again!';
                }
                $scope.proccessing = false;
            });
        } else if(mode == "update"){
            trackerService.update($scope.formModel).success(function (response) {
                $scope.success = response.success;
                if (response.success) {
                    $scope.msg = 'Your tracker has update successfully';
                } else {
                    $scope.error = 'Tracker not exists, please try again!';
                }
                $scope.proccessing = false;
            });
        }
    };

    $scope.initDefaultValue =  function () {
        $scope.msg = "";
        $scope.isDisabled = false;
        $scope.isUpdate = false;
        $scope.success = false;
        $scope.proccessing = false;
        $scope.error = "";
    }

    $scope.initDefaultValue();
}]);