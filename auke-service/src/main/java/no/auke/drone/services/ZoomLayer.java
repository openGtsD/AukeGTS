package no.auke.drone.services;

import java.util.Collection;

import no.auke.drone.domain.MapPoint;

public interface ZoomLayer {

	Collection<MapPoint> getPositions();
	void calculate();
	double zoomLongitude(Double longitude);
	double zoomLatitude(Double latitude);

}