package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Flightpath {

    String orderNo;
    Double fromLongitude;
    Double fromLatitude;
    Double angle;
    Double toLongitude;
    Double toLatitude;
    int ticksSinceStartOfCalculation;

    public Flightpath(String orderNo, Double fromLongitude, Double fromLatitude, Double angle, Double toLongitude, Double toLatitude) {
        this.orderNo = orderNo;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.angle = angle;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;
        //this.ticksSinceStartOfCalculation = ticksSinceStartOfCalculation;
    }

    public static void outputJson(String day, String month, String year, ArrayList<Flightpath> flightpaths) {
        try {

            FileWriter file = new FileWriter("flightpath-" + year + "-" + month + "-" + day + ".json");
            ObjectMapper Obj = new ObjectMapper();
            for (Flightpath flightpath : flightpaths) {
                String jsonStr = Obj.writeValueAsString(flightpath);
                file.write(jsonStr);
            }

            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts a list of LngLat to a FeatureCollection.
     * @param path The path the drone takes.
     * @return The FeatureCollection.
     */
    private ArrayList<String> lnglatToFC(ArrayList<Flightpath> flightpaths){
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        ArrayList<String> json = new ArrayList<>();
        for (Flightpath path : flightpaths) {
            try {
                json.add(ow.writeValueAsString(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

}
