/**
 * Created by thaihuynh on 6/25/2015.
 */
'use strict';

angular.module('aukeGTS').controller('TrackerCtrl', ['$scope', 'TrackerService', 'MapService', 'dateFilter', function ($scope, trackerService, mapService, dateFilter) {

    var service = mapService.mapAPI;

    // init data
    $scope.agentTypes = service.getAgentTypes();
    $scope.agentType = $scope.agentTypes[0];

    $scope.layers = service.getLayers();
    $scope.layer = $scope.layers[0];

    // end init
    $scope.formModel = (typeof $scope.formModel === 'object') ? $scope.formModel : {
        id: '',
        trackerPrefix: '',
        name: '',
        //picture: '',
        owner: '',
        contactInfo: '',
        description: '',
        createDate: '',
        modifiedDate: '',
        layerId: '',
        storedTrips: true,
        //trackerUsage: '',
        //additionalLayers: ''
    };

    $scope.formModel.trackerPrefix = $scope.agentType.value;

    $scope.updateChange = function () {
        $scope.formModel.trackerPrefix = $scope.agentType.value;
    }

    $scope.updateChangeLayer = function () {
        $scope.formModel.layerId = $scope.layer.value;
    }

    $scope.stepsModel = [];
    $scope.imageUpload = function (element) {
        var reader = new FileReader();
        reader.onload = $scope.imageIsLoaded;
        reader.readAsDataURL(element.files[0]);

    }

    $scope.imageIsLoaded = function (e) {
        $scope.stepsModel = [];
        $scope.$apply(function () {
            $scope.stepsModel.push(e.target.result);
        });
    }

    $scope.updateTracker = function (mode) {
        $scope.error = "";
        $scope.proccessing = true;

        if ($scope.formModel.id == "") {
            $scope.success = false;
            $scope.error = "This field is required";
            $scope.proccessing = false;
            return;
        }
         if (mode === "register") {
             trackerService.create($scope.formModel).success(function (response) {
                $scope.success = response.success;
                if (response.success) {
                    $scope.msg = 'Your tracker has been create successfully.';
                } else {
                    $scope.error = 'Please check make sure your services is running.';
                }
                $scope.proccessing = false;
            });
        } else if (mode === "delete") {
             trackerService.delete($scope.formModel.id).success(function (response) {
                $scope.success = response.success;
                if (response.success) {
                    $scope.msg = 'Your tracker has delete successfully';
                } else {
                    $scope.error = 'Tracker not exists, please try again!';
                }
                $scope.proccessing = false;

            });
        } else if (mode == "load") {
             trackerService.load($scope.formModel.id).success(function (response) {
                $scope.success = false;
                if (response.success) {
                    $scope.isDisabled = true;
                    $scope.isUpdate = true;
                    var data = response.data;
                    $scope.mode = "update";
                    $scope.formModel = data[0];

                    // binding data
                    $scope.agentType = ($scope.formModel.trackerPrefix !== '' && $scope.agentTypes[0].value == $scope.formModel.trackerPrefix) ? $scope.agentTypes[0] : $scope.agentTypes[1];
                    $scope.layer = ($scope.formModel.layerId !== '' && $scope.layers[0].value == $scope.formModel.layerId) ? $scope.layers[0] : $scope.layers[1];

                    $scope.formModel.createDate = dateFilter(data[0].createDate, 'yyyy-MM-dd');
                    $scope.formModel.modifiedDate = dateFilter(data[0].modifiedDate, 'yyyy-MM-dd');
                } else {
                    $scope.error = 'Tracker not exists, please try again!';
                }
                $scope.proccessing = false;
            });
        } else if (mode == "update") {
             $scope.formModel.createDate = "";
             $scope.formModel.modifiedDate = "";
             trackerService.update($scope.formModel).success(function (response) {
                 $scope.success = response.success;
                 if (response.success) {
                     $scope.msg = 'Your tracker has update successfully';
                     $scope.formModel.createDate = dateFilter(response.data[0].createDate, 'yyyy-MM-dd');
                     $scope.formModel.modifiedDate = dateFilter(response.data[0].modifiedDate, 'yyyy-MM-dd');
                 } else {
                     $scope.error = 'Tracker not exists, please try again!';
                 }
                 $scope.proccessing = false;
             });
         }
    };

    $scope.initDefaultValue = function () {
        $scope.msg = "";
        $scope.isDisabled = false;
        $scope.isUpdate = false;
        $scope.success = false;
        $scope.proccessing = false;
        $scope.error = "";
    }

    $scope.initDefaultValue();
}]);