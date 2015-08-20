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



    mapAPI.getTrips = function() {
        return mapAPI.listTrip;
    }

    mapAPI.getTripInfo = function() {
        return mapAPI.tripInfo;
    }

    mapAPI.getTripByTrackerId = function(trackerId){
        TrackerService.getTripByTrackerId(trackerId).success(function (response) {
            if (response.data && response.data.length > 0) {
                mapAPI.listTrip = response.data;
            }
        })
    }

    mapAPI.getTripById = function(id) {
        TrackerService.getTripById(id).success(function (response) {
            if (response.data && response.data.length > 0) {
                mapAPI.tripInfo = response.data[0];
            }
        })
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
                    mapAPI.window.show = false;
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
            id: tracker.id,//TODO modify into UUID
            icon: mapAPI.createIcon(),
            latitude: tracker.currentPosition.latitude,
            longitude: tracker.currentPosition.longitude,
            name: tracker.name,
            moving: tracker.moving,
            altitude: tracker.currentPosition.altitude,
            time: new Date(tracker.currentPosition.time * 1000),
            speed: tracker.currentPosition.speed

        }

        return marker;
    };

    mapAPI.createTripIcon = function(lat, lon, i) {
        return {
            id: i,
            latitude: lat,
            longitude: lon
            //icon: createIcon(start, stop),
        }
    }

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
    mapAPI.showTrip = false;

    // ui trips
    mapAPI.trackerId = '';
    mapAPI.listTrip = [];
    mapAPI.tripInfo = '';

    mapAPI.init = function () {
        return {
            'mapAPI': mapAPI,
            map: {
                templateUrl: TrackerService.getBaseURL() + '/app/scripts/ui/home/info.tpl.html',
                templateParameter: function(model) {
                    return model;
                },
                center: {
                    latitude: 0,
                    longitude: 0
                },
                zoom: 3,
                markersEvents: { // event marker
                    click: function (marker, eventName, model) {
                        if (mapAPI.map.getZoom() < 11) {
                            mapAPI.setCenter(model);
                        } else {
                            TrackerService.load(model.id).success(function (response) {
                                if (response.data && response.data.length > 0) {
                                    mapAPI.window.model = model;
                                    mapAPI.window.show = true;
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
                        mapAPI.window.show = false;

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




