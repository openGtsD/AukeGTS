package no.auke.drone.services;

import java.util.Collection;
import java.util.List;

import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.Tracker;

public interface ZoomLayerService {

	void calculate();
	
	double zoomLongitude(Double longitude);
	double zoomLatitude(Double latitude);
	double longitude(Double zoomlongitude);
	double latitude(Double zoomlatitude);	
	double latitudeWith();
	double longitudeWith();
	
	int getZoomFactor();
	void clear();
	
	Collection<Tracker> loadWithinView(BoundingBox boundary, int zoom);
	Collection<Tracker> getPositions();
	List<BoundingBox> getMapAreas();





}