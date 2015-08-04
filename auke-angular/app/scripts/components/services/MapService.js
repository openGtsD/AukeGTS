'use strict';

angular.module('aukeGTS').factory('MapService', function ($http, $timeout, $interval, TrackerService) {
    var mapAPI = {};

    mapAPI.setLayer = function (layer) {
        mapAPI.layer = layer;
    }
    mapAPI.getLayer = function () {
        return mapAPI.layer;
    }
    mapAPI.setUUID = function (uuid) {
        mapAPI.uuid = uuid;
    }

    mapAPI.getMarkers = function () {
        return mapAPI.markers;
    }

    mapAPI.getLayers = function () {
        //TODO: we will call API for get all layers in systems at here
        return [
            {
                name: 'SIMULATED',
                value: 'SIMULATED'
            },
            {
                name: 'REAL',
                value: 'REAL'
            }
        ];
    }

    mapAPI.getAgentTypes = function () {
        //TODO: we will call API for get all agent support from openGTS
        return [
            {
                name: 'TK10X',
                value: 'tk'
            },
            {
                name: 'GPRMC',
                value: 'gprmc'
            }
        ];
    }
    mapAPI.loadDroneWithinCurrentView = function () {
        var map = mapAPI.map;
        var mapBound = map.getBounds();
        var zoom = map.getZoom();
        var uuid = mapAPI.uuid;
        if (uuid != null && uuid != '') {
            var markers = [];
            TrackerService.load(uuid).success(function (response) {
                if (response.data && response.data.length > 0) {
                    var marker = mapAPI.createMarker(response.data[0]);
                    markers.push(marker);
                    mapAPI.markers = markers;

                    var posn = new google.maps.LatLng(response.data[0].currentPosition.latitude,
                        response.data[0].currentPosition.longitude);
                    var bounds = new google.maps.LatLngBounds();
                    bounds.extend(posn);
                    map.fitBounds(bounds);
                } else {
                    mapAPI.markers = [];
                }
            });

            mapAPI.uuid = '';

            $('#zoomId').text(zoom);
            $("#type").text(zoom >= 11 ? "drone" : "position");
            $("#trackerNumber").text(mapAPI.markers.length);
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

            TrackerService.loadDroneWithinView(dataObject, mapAPI.layer.value, zoom).success(function (response) {
                var markers = [];
                if (response.data.length > 0) {
                    var data = response.data;
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].currentPosition.latitude != 0 && data[i].currentPosition.longitude != 0) {
                            var marker = mapAPI.createMarker(data[i]);
                            markers.push(marker);
                        }
                    }
                } else {
                    // Sometimes when drone moving, Dialog not hidden
                    mapAPI.reMakeInfoBubble();
                }
                mapAPI.markers = markers;
                $('#zoomId').text(zoom);
                $("#type").text(zoom >= 11 ? "drone" : "position");
                $("#trackerNumber").text(mapAPI.markers.length);
            });
        }
    };

    mapAPI.createMarker = function (tracker) {
        var marker = {
            id: tracker.id,
            icon: mapAPI.createIcon(),
            latitude: tracker.currentPosition.latitude,
            longitude: tracker.currentPosition.longitude,
            showWindow: false,
            title: tracker.name
        }

        return marker;
    };

    mapAPI.setCenter = function (tracker) {

        var posn = new google.maps.LatLng(tracker.latitude,
            tracker.longitude);
        var bounds = new google.maps.LatLngBounds();
        bounds.extend(posn);
        mapAPI.map.fitBounds(bounds);
    };

    mapAPI.createIcon = function () {
        return TrackerService.getBaseURL() + '/app/images/flight.gif';
    };

    mapAPI.buildContent = function (data, type) {
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

    mapAPI.makeNewInfoBuddle = function () {
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

    mapAPI.reMakeInfoBubble = function () {
        if (mapAPI.infoBubble) {
            mapAPI.infoBubble.close();
            delete mapAPI.infoBubble;
            mapAPI.infoBubble = mapAPI.makeNewInfoBuddle();
        }
    };


    //init properties
    mapAPI.uuid = '';
    mapAPI.markers = [];
    mapAPI.window = {
        marker: {},
        show: false,
        closeClick: function () {
            this.show = false;
        },
        options: {} // define when map is ready
    };
    mapAPI.layer = '';
    mapAPI.map = '';

    mapAPI.infoBubble = mapAPI.makeNewInfoBuddle();

    mapAPI.init = function () {
        return {
            'mapAPI': mapAPI,
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
                        if (mapAPI.map.getZoom() < 11) {
                            mapAPI.setCenter(model);
                        } else {
                            TrackerService.load(model.id).success(function (response) {
                                if (response.data && response.data.length > 0) {
                                    var div = document.createElement('DIV');
                                    div.innerHTML = mapAPI.buildContent(response.data[0], 'info');
                                    mapAPI.reMakeInfoBubble();
                                    mapAPI.infoBubble.open(mapAPI.map, marker);
                                    mapAPI.infoBubble.addTab('Tracker Information', div);
                                    mapAPI.infoBubble.addTab('Flyer Information', 'Comming soon...');
                                }
                            });
                        }
                    }
                },
                options: {},
                control: {},
                events: { // event map
                    tilesloaded: function (maps, eventName, args) {
                        mapAPI.map = maps;
                    },
                    dragend: function (maps, eventName, args) {
                        mapAPI.map = maps;
                    },
                    zoom_changed: function (maps, eventName, args) {
                        mapAPI.map = maps;
                        mapAPI.reMakeInfoBubble();
                        mapAPI.loadDroneWithinCurrentView();

                    },
                    idle: function (maps, eventName, args) {
                        mapAPI.map = maps;

                        mapAPI.loadDroneWithinCurrentView();
                    }
                },
                window: mapAPI.window
            }
        };
    }
    return mapAPI.init();
});




