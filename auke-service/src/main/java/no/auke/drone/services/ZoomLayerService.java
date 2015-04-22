package no.auke.drone.services;

public interface ZoomLayerService {

	void calculate();
	double zoomLongitude(Double longitude);
	double zoomLatitude(Double latitude);
	int getZoomFactor();
	void clear();

}