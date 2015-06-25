'use strict';

angular.module('aukeGTS').constant('HomeCtrl', {
  name: 'HomeCtrl',
  resolve: {}
});

angular.module('aukeGTS').controller('HomeCtrl',[ '$scope', 'trackerService', 'uiGmapGoogleMapApi', '$timeout', '$interval', 'uiGmapIsReady', function ($scope, trackerService, uiGmapGoogleMapApi, $timeout, $interval, uiGmapIsReady) {
    $scope.sizes = [
        {
            name: 'REAL',
            value: 'REAL'
        },
        {
            name: 'SIMULATED',
            value: 'SIMULATED'
        }
    ];
    $scope.item = $scope.sizes[0];
    var layer = $scope.item.value;
    $scope.update = function() {
        layer = $scope.item.value;
        $scope.reMakeInfoBubble();
        $scope.loadDroneWithinView($scope.myMap);
    }

    uiGmapGoogleMapApi.then(function(maps){
                $scope.markers = [];
                $scope.myMap = "";
                $scope.map = {
                    center: {
                        latitude: 0,
                        longitude: 0
                    },
                    zoom: 3,
                    minZoom : 3,
                    options: {},
                    control: {},

                    events: {
                        tilesloaded: function (maps, eventName, args) {
                        },
                        dragend: function (maps, eventName, args) {
                        },
                        zoom_changed: function (maps, eventName, args) {
                            $scope.myInterval = $interval(function(){
                                if(maps.getZoom() >= 11) {
                                    $scope.loadDroneWithinView(maps);
                                } else {
                                    clearInterval($scope.myInterval);
                                    $scope.myInterval = null;
                                }
                            }, 10000);
                        },
                        idle: function (maps, eventName, args) {
                            $scope.myMap = maps;
                            $scope.loadDroneWithinView(maps);
                        }
                    }
                };
        });

        $scope.loadDroneWithinView = function(map) {
            var mapBound = map.getBounds();
            var zoom = map.getZoom();
            //if(zoom < 11) {
                $scope.reMakeInfoBubble();
            //}
            var ne = mapBound.getNorthEast(); // LatLng of the north-east corner
            var sw = mapBound.getSouthWest();
            var dataObject = {
                southWestLat : sw.lat(),
                southWestLon : sw.lng(),
                northEastLat : ne.lat(),
                northEastLon : ne.lng()
            };

            trackerService.loadDroneWithinView(dataObject, layer, zoom).success(function (response) {
                var markers = [];
                if (response.data.length > 0) {

                    var data = response.data;
                    for (var i = 0; i < data.length; i++) {
                        var marker = $scope.createMarker(data[i]);
                        markers.push(marker);
                    }
                }
                $scope.markers = markers;
                $('#zoomId').text(zoom);
                $("#type").text(zoom >= 11 ? "drone" : "position");
                $("#trackerNumber").text($scope.markers.length);
            });

        };

        $scope.createMarker = function (tracker) {
            var marker = {
                 id: tracker.id,
                 icon: $scope.myMap.getZoom() >= 11 ? 'app/images/flight.gif' : '',
                 latitude: tracker.currentPosition.latitude,
                 longitude: tracker.currentPosition.longitude,
                 showWindow: false,
                 numbertracker: tracker.numtrackers,
                 title : tracker.name,
                 options: {
                        labelContent:  tracker.name
                 },
                 events:{
                    dblclick: function(){
                        var posn = new google.maps.LatLng(tracker.currentPosition.latitude,
                            tracker.currentPosition.longitude);
                        var bounds = new google.maps.LatLngBounds();
                        bounds.extend(posn);
                        $scope.myMap.fitBounds(bounds);
                        $scope.myMap.setZoom(11);
                    },
                    click : function(marker) {
                        if($scope.myMap.getZoom() < 11) {
                            var div = document.createElement('DIV');
                            div.innerHTML = '<div>Number tracker is: ' + tracker.numtrackers + '</div>';
                            $scope.reMakeInfoBubble();
                            $scope.infoBubble.open($scope.myMap, marker);
                            $scope.infoBubble.addTab("Position Information", div);
                        } else {
                            $scope.getTracker(marker, $scope.myMap);
                        }
                    }
                 }
            }

            return marker;
        };


     //var onMarkerClicked = function (marker) {
    //       $scope.reMakeInfoBubble();
    //       //$timeout(function () {
    //           if ($scope.myMap.getZoom() >= 11) {
    //               marker.closeClick = function () {
    //                   marker.showWindow = false;
    //                   $scope.$evalAsync();
    //               };
    //
    //               $scope.getTracker(marker, $scope.myMap);
    //           } else {
    //               marker.showWindow = true;
    //               $scope.$apply();
    //           }
    //       //});
    //};
    //$scope.onMarkerClicked = onMarkerClicked;

    $scope.getTracker = function(marker, map) {
        trackerService.getTracker(marker, map).success(function (response) {
            if (response.data && response.data.length > 0) {
                var div = document.createElement('DIV');
                div.innerHTML = $scope.buildContent(response.data[0]);
                $scope.reMakeInfoBubble();
                $scope.infoBubble.open(map, marker);
                $scope.infoBubble.addTab("Tracker Information", div);
                $scope.infoBubble.addTab("Flyer Information", "Comming soon...");
            }
        });
    };

    $scope.buildContent = function(data) {
        return "<ul>" + "<li>Id: <span class='highlight'>" + data.id
            + "</span></li><li>GPS: <span class='highlight'>"
            + data.currentPosition.latitude + " / "
            + data.currentPosition.longitude
            + "</span></li><li>Altitude: <span class='highlight'>"
            + data.currentPosition.altitude
            + "</span></li><li>Speed: <span class='highlight'>"
            + data.currentPosition.speed + "</span></li>"
            + "<li>Moving: <span class='highlight'>" + data.moving
            + "</span></li> <li>Name: <span class='highlight'>" + data.name
            + "</span></li><li>Time: <span class='highlight'>"
            + new Date(data.currentPosition.time * 1000) + "</li></ul>"
    };


    $scope.makeNewInfoBuddle = function() {
        var info = new InfoBubble({
                shadowStyle : 1,
                padding : 10,
                borderRadius : 5,
                minWidth : 200,
                borderWidth : 1,
                disableAutoPan : true,
                hideCloseButton : false
        });
        return info;
    };

    $scope.reMakeInfoBubble = function() {
        if ($scope.infoBubble) {
            $scope.infoBubble.close();
            delete $scope.infoBubble;
            $scope.infoBubble = $scope.makeNewInfoBuddle();
        }
    };

    $scope.infoBubble = $scope.makeNewInfoBuddle();

    google.maps.event.addDomListener(window, "resize", function() {
        //$('#container').css("height",$(window).height());
        //$('#container').css("width",$(window).width());
        //var center = $scope.myMap.getCenter();
        //google.maps.event.trigger($scope.myMap, "resize");
        //$scope.myMap.setCenter(center);
    });
}]);
