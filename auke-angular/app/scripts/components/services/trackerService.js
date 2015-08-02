'use strict';

angular.module('aukeGTS').factory('trackerService', function ($http, aukeUtil, $timeout, $interval) {
    var serviceURL = aukeUtil.serviceURL;
    var trackerAPI = {};

    //API


    trackerAPI.loadDroneWithinView = function (json, layerId, zoom) {
        var url = serviceURL + '/drone/load-drone-in-view/' + layerId + '/' + zoom;
        return $http.post(url, json);
    }
    trackerAPI.create = function (tracker) {
        var url = serviceURL + '/drone/registertk/';
        return $http.post(url, tracker);
    }

    trackerAPI.delete = function (id) {
        var url = serviceURL + '/drone/remove/' + id;
        return $http.post(url);
    }

    trackerAPI.load = function (id) {
        var url = serviceURL + '/drone/get-tracker/' + id;
        return $http.post(url);
    }

    trackerAPI.update = function (tracker) {
        var url = serviceURL + '/drone/update';
        return $http.post(url, tracker);
    }

    trackerAPI.setLayer = function(layer) {console.log(layer);
        trackerAPI.layer = typeof(layer) != undefined ? trackerAPI.layers[0] : layer;
        console.log(trackerAPI.layer)
    }

    trackerAPI.setUUID = function(uuid) {
        trackerAPI.uuid = uuid;
    }

    trackerAPI.loadDroneWithinCurrentView = function () {
        var map = trackerAPI.map;
        var mapBound = map.getBounds();
        var zoom = map.getZoom();
        var uuid = trackerAPI.uuid;
        if (uuid != null && uuid != '') {
            var markers = [];
            trackerAPI.load(uuid).success(function (response) {
                if (response.data && response.data.length > 0) {
                    var marker = trackerAPI.createMarker(response.data[0]);
                    markers.push(marker);
                    trackerAPI.markers = markers;

                    var posn = new google.maps.LatLng(response.data[0].currentPosition.latitude,
                        response.data[0].currentPosition.longitude);
                    var bounds = new google.maps.LatLngBounds();
                    bounds.extend(posn);
                    map.fitBounds(bounds);
                } else {
                    trackerAPI.markers = [];
                }
            });

            trackerAPI.uuid = '';

            $('#zoomId').text(zoom);
            $("#type").text(zoom >= 11 ? "drone" : "position");
            $("#trackerNumber").text(trackerAPI.markers.length);
        }
        else {
            var ne = mapBound.getNorthEast(); // LatLng of the north-east corner
            var sw = mapBound.getSouthWest();
            var dataObject = {
                southWestLat: sw.lat(),
                southWestLon: sw.lng(),
                northEastLat: ne.lat(),
                northEastLon: ne.lng()
            };

            trackerAPI.loadDroneWithinView(dataObject, trackerAPI.layer.value, zoom).success(function (response) {
                var markers = [];
                if (response.data.length > 0) {
                    var data = response.data;
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].currentPosition.latitude != 0 && data[i].currentPosition.longitude != 0) {
                            var marker = trackerAPI.createMarker(data[i]);
                            markers.push(marker);
                        }
                    }
                } else {
                    // Sometimes when drone moving, Dialog not hidden
                    trackerAPI.reMakeInfoBubble();
                }
                trackerAPI.markers = markers;
                $('#zoomId').text(zoom);
                $("#type").text(zoom >= 11 ? "drone" : "position");
                $("#trackerNumber").text(trackerAPI.markers.length);
            });
        }
    };

    trackerAPI.createMarker = function (tracker) {
        var marker = {
            id: tracker.id,
            icon: trackerAPI.createIcon(tracker),
            latitude: tracker.currentPosition.latitude,
            longitude: tracker.currentPosition.longitude,
            showWindow: false,
            //numbertracker: tracker.numtrackers,
            title: tracker.name
        }

        return marker;
    };

    trackerAPI.setCenter = function (tracker) {

        var posn = new google.maps.LatLng(tracker.latitude,
            tracker.longitude);
        var bounds = new google.maps.LatLngBounds();
        bounds.extend(posn);
        trackerAPI.map.fitBounds(bounds);
    };

    trackerAPI.createIcon = function (tracker) {
        return aukeUtil.baseURL + '/app/images/flight.gif';
    };

    trackerAPI.buildContent = function (data, type) {
        if (type === "info") {
            return "<ul><li>GPS: <span class='highlight'>"
                + data.currentPosition.latitude + " / " + data.currentPosition.longitude
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

    trackerAPI.makeNewInfoBuddle = function () {
        var info = new InfoBubble({
            shadowStyle: 0,
            padding: 10,
            borderRadius: 5,
            borderWidth: 1,
            //maxHeight: 250,
            maxWidth: 350,
            arrowSize: 15
        });
        return info;
    };

    trackerAPI.reMakeInfoBubble = function () {
        if (trackerAPI.infoBubble) {
            trackerAPI.infoBubble.close();
            delete trackerAPI.infoBubble;
            trackerAPI.infoBubble = trackerAPI.makeNewInfoBuddle();
        }
    };

    trackerAPI.getMarkers = function () {
        return trackerAPI.markers;
    }

    //init
    trackerAPI.uuid = '';
    trackerAPI.markers = [];
    trackerAPI.window = {
        marker: {},
        show: false,
        closeClick: function () {
            alert('window click');
            this.show = false;
        },
        options: {} // define when map is ready
    };
    trackerAPI.layers = [
        {
            name: 'SIMULATED',
            value: 'SIMULATED'
        },
        {
            name: 'REAL',
            value: 'REAL'
        }
    ];

    trackerAPI.map = '';

    trackerAPI.infoBubble = trackerAPI.makeNewInfoBuddle();

    trackerAPI.init = function () {
        return {
            map: {
                center: {
                    latitude: 0,
                    longitude: 0
                },
                zoom: 3,
                markersEvents: { // event marker
                    click: function (marker, eventName, model) {
                        //trackerAPI.window.model = model;
                        //trackerAPI.window.show = true;
                        if (trackerAPI.map.getZoom() < 11) {
                            trackerAPI.setCenter(model);
                        } else {
                            trackerAPI.load(model.id).success(function (response) {
                                if (response.data && response.data.length > 0) {
                                    var div = document.createElement('DIV');
                                    div.innerHTML = trackerAPI.buildContent(response.data[0], "info");
                                    trackerAPI.reMakeInfoBubble();
                                    trackerAPI.infoBubble.open(trackerAPI.map, marker);
                                    trackerAPI.infoBubble.addTab("Tracker Information", div);
                                    trackerAPI.infoBubble.addTab("Flyer Information", "Comming soon...");
                                }
                            });
                        }
                    }
                },
                options: {},
                control: {},
                layer: trackerAPI.layer,
                layers: trackerAPI.layers,
                events: { // event map
                    tilesloaded: function (maps, eventName, args) {
                        trackerAPI.map = maps;
                    },
                    dragend: function (maps, eventName, args) {
                        trackerAPI.map = maps;
                    },
                    zoom_changed: function (maps, eventName, args) {
                        trackerAPI.map = maps;
                        trackerAPI.reMakeInfoBubble();
                        var zoom = maps.getZoom();
                        trackerAPI.myInterval = $interval(function () {
                            zoom = maps.getZoom();
                            if (zoom >= 11) {
                                trackerAPI.loadDroneWithinCurrentView();
                            } else {
                                clearInterval(trackerAPI.myInterval);
                                trackerAPI.myInterval = null;
                            }
                        }, 10000);
                    },
                    idle: function (maps, eventName, args) {
                        trackerAPI.map = maps;

                        trackerAPI.loadDroneWithinCurrentView();
                    }
                },
                window: trackerAPI.window
            },
            trackerAPI
        }
    }
    return trackerAPI.init();
});




