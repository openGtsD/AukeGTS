'use strict';

angular.module('aukeGTS').constant('HomeCtrl', {
    name: 'HomeCtrl',
    resolve: {}
});

angular.module('aukeGTS').controller('HomeCtrl', ['$scope', '$stateParams', 'trackerService', 'uiGmapGoogleMapApi', '$timeout', '$interval', 'uiGmapIsReady', 'aukeUtil', function ($scope, $stateParams, trackerService, uiGmapGoogleMapApi, $timeout, $interval, uiGmapIsReady, aukeUtil) {
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
    var layerParam = $stateParams.layer;
    if (layerParam == null || layerParam == '') {
        $scope.item = $scope.sizes[0];
    } else {
        $scope.item = (layerParam !== '' && $scope.sizes[0].value == layerParam) ? $scope.sizes[0] : $scope.sizes[1];
    }
    $scope.layer = $scope.item.value;


    $scope.update = function () {
        $scope.layer = $scope.item.value;
        $scope.reMakeInfoBubble();
        $scope.loadDroneWithinView($scope.myMap);
    }

    uiGmapGoogleMapApi.then(function (maps) {
        $scope.markers = [];
        $scope.myMap = "";
        $scope.map = {
            center: {
                latitude: 0,
                longitude: 0
            },
            zoom: 3,
            minZoom: 3,
            options: {},
            control: {},

            events: {
                tilesloaded: function (maps, eventName, args) {
                },
                dragend: function (maps, eventName, args) {
                },
                zoom_changed: function (maps, eventName, args) {
                    var zoom = maps.getZoom();
                    $scope.myInterval = $interval(function () {
                        zoom = maps.getZoom();
                        if (zoom >= 11) {
                            //$scope.layer = $scope.item.value;
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

    $scope.loadDroneWithinView = function (map) {
        var mapBound = map.getBounds();
        var zoom = map.getZoom();
        if ($stateParams.id != null && $stateParams.id != '') {
            var markers = [];
            trackerService.load($stateParams.id).success(function (response) {
                if (response.data && response.data.length > 0) {
                    var marker = $scope.createMarker(response.data[0]);
                    markers.push(marker);
                    $scope.markers = markers;

                    var posn = new google.maps.LatLng(response.data[0].currentPosition.latitude,
                        response.data[0].currentPosition.longitude);
                    var bounds = new google.maps.LatLngBounds();
                    bounds.extend(posn);
                    map.fitBounds(bounds);
                }
            });

            $stateParams.id = '';

            $('#zoomId').text(zoom);
            $("#type").text(zoom >= 11 ? "drone" : "position");
            $("#trackerNumber").text($scope.markers.length);
        } else {
            //if(zoom < 11) {
            //    $scope.reMakeInfoBubble();
            //}
            var ne = mapBound.getNorthEast(); // LatLng of the north-east corner
            var sw = mapBound.getSouthWest();
            var dataObject = {
                southWestLat: sw.lat(),
                southWestLon: sw.lng(),
                northEastLat: ne.lat(),
                northEastLon: ne.lng()
            };

            trackerService.loadDroneWithinView(dataObject, $scope.layer, zoom).success(function (response) {
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
        }
    };

    $scope.createMarker = function (tracker) {
        var marker = {
            id: tracker.id,
            icon: $scope.myMap.getZoom() >= 11 ? aukeUtil.baseURL + '/app/images/flight.gif' : '',
            latitude: tracker.currentPosition.latitude,
            longitude: tracker.currentPosition.longitude,
            showWindow: false,
            numbertracker: tracker.numtrackers,
            title: tracker.name,
            options: {
                labelContent: tracker.name
            },
            events: {
                dblclick: function () {
                    var posn = new google.maps.LatLng(tracker.currentPosition.latitude,
                        tracker.currentPosition.longitude);
                    var bounds = new google.maps.LatLngBounds();
                    bounds.extend(posn);
                    $scope.myMap.fitBounds(bounds);
                    //$scope.myMap.setZoom(11);
                },
                click: function (marker) {
                    if ($scope.myMap.getZoom() < 11) {
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

    $scope.getTracker = function (marker, map) {
        trackerService.getTracker(marker, map).success(function (response) {
            if (response.data && response.data.length > 0) {
                var div = document.createElement('DIV');
                div.innerHTML = $scope.buildContent(response.data[0], "info");
                var div2 = document.createElement('DIV');
                div2.innerHTML = $scope.buildContent(response.data[0], "trip");
                $scope.reMakeInfoBubble();
                $scope.infoBubble.open(map, marker);
                $scope.infoBubble.addTab("Tracker Information", div);
                $scope.infoBubble.addTab("Trips Information", div2);
                $scope.infoBubble.addTab("Flyer Information", "Comming soon...");
            }
        });
    };

    $scope.buildContent = function (data, type) {console.log(data)
        if (type === "info") {
            return "<ul>" + "<li>Id: <span class='highlight'>" + data.id
                + "</span></li><li>GPS: <span class='highlight'>"
                + data.currentPosition.latitude + " / "+ data.currentPosition.longitude
                + "</span></li><li>Altitude: <span class='highlight'>"
                + data.currentPosition.altitude
                + "</span></li><li>Speed: <span class='highlight'>"
                + data.currentPosition.speed + "</span></li>"
                + "<li>Moving: <span class='highlight'>" + data.moving
                + "</span></li> <li>Name: <span class='highlight'>" + data.name
                + "</span></li><li>Time: <span class='highlight'>"
                + new Date(data.currentPosition.time * 1000) + "</li></ul>";
        } else if (type === "trip") {
            var content = "";
            for (var i = 0; i < data.latestPositions.length; i++) {
                content += "<li> Time: " + new Date(data.latestPositions[i].time * 1000) + "</li>" +
                    "<li> Altitude: " + data.latestPositions[i].altitude + "</li>" +
                    "<li> GPS: " + data.latestPositions[i].latitude + " / " + data.latestPositions[i].longitude + "</li>" +
                    "<li> Speed: " + data.latestPositions[i].speed + "</li>" +
                    "<hr>"
            }
            return "<ul>" + content + "</ul>";
        }
    };


    $scope.makeNewInfoBuddle = function () {
        var info = new InfoBubble({
            shadowStyle: 1,
            padding: 10,
            borderRadius: 5,
            minWidth: 200,
            borderWidth: 1,
            disableAutoPan: true,
            hideCloseButton: false,
            maxHeight: 250
        });
        return info;
    };

    $scope.reMakeInfoBubble = function () {
        if ($scope.infoBubble) {
            $scope.infoBubble.close();
            delete $scope.infoBubble;
            $scope.infoBubble = $scope.makeNewInfoBuddle();
        }
    };

    $scope.infoBubble = $scope.makeNewInfoBuddle();

    google.maps.event.addDomListener(window, "resize", function () {
        //$('#container').css("height",$(window).height());
        //$('#container').css("width",$(window).width());
        //var center = $scope.myMap.getCenter();
        //google.maps.event.trigger($scope.myMap, "resize");
        //$scope.myMap.setCenter(center);
    });

}]);
