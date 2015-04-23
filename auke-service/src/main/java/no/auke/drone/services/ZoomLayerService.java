package no.auke.drone.services;

import java.util.Collection;
import java.util.List;

import no.auke.drone.domain.BoundingBox;
import no.auke.drone.domain.Tracker;

public interface ZoomLayerService {

	void calculate();
	double zoomLongitude(Double longitude);
	double zoomLatitude(Double latitude);
	int getZoomFactor();
	void clear();
	
	List<Tracker> loadWithinView(BoundingBox boundary, int zoom);
	Collection<Tracker> getPositions();	

}