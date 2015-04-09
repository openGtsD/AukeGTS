package no.auke.drone.utils;

import com.google.gson.Gson;
import no.auke.drone.domain.MapPoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by huyduong on 4/4/2015.
 */
public class LocationFunction {
    
	private static final Logger logger = LoggerFactory.getLogger(LocationFunction.class);
    private static String droneFolder = "dronedata";

    static {
        
    	// check or create folder
        File droneFolder = new File("dronedata");
        
        if(!droneFolder.exists()) {
            droneFolder.mkdir();
        }

    }

    static class MapPointData {
    	
        private String droneId;
        private Long tripTime = System.currentTimeMillis();
        
        public Long getTripTime() {
			return tripTime;
		}

		public void setTripTime(Long tripTime) {
			this.tripTime = tripTime;
		}

		private List<MapPoint> mapPoints;

        public MapPointData() {};

        public MapPointData(String droneId, List<MapPoint> mapPoints) {
            this.droneId = droneId;
            this.mapPoints = mapPoints;
        }

        public MapPointData(String droneId, MapPoint mapPoint) {
            this.droneId = droneId;
            this.mapPoints = new ArrayList<MapPoint>();
            this.mapPoints.add(mapPoint);
        }

        public String getDroneId() {
            return droneId;
        }

        public void setDroneId(String droneId) {
            this.droneId = droneId;
        }

        public List<MapPoint> getMapPoints() {
            return mapPoints;
        }

        public void setMapPoints(List<MapPoint> mapPoints) {
            this.mapPoints = mapPoints;
        }
    }

    public static MapPointData readLocationHistoryByDroneId(String droneId) throws IOException {
        
    	// 
    	// this is trips
    	// fix
    	// 
    	
    	String output = "drone-" + droneId + ".json";
        File dataFile = new File(droneFolder,output);

        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader(dataFile));

        MapPointData result = gson.fromJson(br, MapPointData.class);
        return result;
    
    }

    public static void writeLocationHistoryByDroneId(String droneId, MapPoint mapPoint) {
        
    	try {

            // this become to big
            // save in trips

    		
        	Gson gson = new Gson();
            MapPointData mapPointData = new MapPointData(droneId,mapPoint);
            
            
            
            String output = "drone-" + droneId + "-" + String.valueOf(mapPointData.getTripTime()) + ".json";
            File dataFile = new File(droneFolder,output);
            
            if(dataFile.exists()) {
            
            	BufferedReader br = new BufferedReader(new FileReader(dataFile));
                
            	mapPointData = gson.fromJson(br, MapPointData.class);
                mapPointData.getMapPoints().add(mapPoint);
            
            } else {
            
                dataFile.createNewFile();
            }

            String json = gson.toJson(mapPointData);
            FileWriter writer = new FileWriter(dataFile);
            writer.write(json);
            writer.close();
            
        } catch (Exception e) {
            
        	logger.error("error writing location history ", e);
        }
    }
}
