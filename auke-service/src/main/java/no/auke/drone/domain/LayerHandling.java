package no.auke.drone.domain;

import java.util.List;

public interface LayerHandling {

	List<Tracker> loadWithinView(BoundingBox boundary, int zoom);
	
	

}
