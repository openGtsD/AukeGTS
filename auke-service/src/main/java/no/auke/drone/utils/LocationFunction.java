package no.auke.drone.utils;

import com.google.gson.Gson;
import no.auke.drone.domain.MapPoint;
import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
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

    private static class MapPointData {
        private String droneId;
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
        String output = "drone-" + droneId + ".json";
        File dataFile = new File(droneFolder,output);

        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(
                new FileReader(dataFile));

        MapPointData result = gson.fromJson(br, MapPointData.class);
        return result;
    }

    public static void writeLocationHistoryByDroneId(String droneId, MapPoint mapPoint) {
        try {
            Gson gson = new Gson();
            MapPointData mapPointData = null;
            String output = "drone-" + droneId + ".json";
            File dataFile = new File(droneFolder,output);
            if(dataFile.exists()) {
                BufferedReader br = new BufferedReader(
                        new FileReader(dataFile));
                mapPointData = gson.fromJson(br, MapPointData.class);
            } else {
                mapPointData = new MapPointData(droneId,mapPoint);
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
