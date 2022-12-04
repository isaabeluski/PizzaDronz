package uk.ac.ed.inf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.FeatureCollection;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GeoJson {

    /**
     * Creates a GeoJson file from a list of lines.
     * @param day The day of the month.
     * @param month The month of the year.
     * @param year  The year.
     * @param path  The path the drone takes.
     */
    public void outputGeoJson(String day, String month, String year, ArrayList<LngLat> path) {
        try {

            FileWriter file = new FileWriter("resultfiles/drone-" + year + "-" + month + "-" + day + ".geojson");
            file.write(lnglatToFC(path).toJson());
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts a list of LngLat to a FeatureCollection.
     * @param path The path the drone takes.
     * @return The FeatureCollection.
     */
    private FeatureCollection lnglatToFC(ArrayList<LngLat> path){
        ArrayList<Point> points = new ArrayList<>();

        for(LngLat move: path){
            points.add(Point.fromLngLat(move.lng(), move.lat()));
        }

        Feature featureLineString =  Feature.fromGeometry(LineString.fromLngLats(points));
        return FeatureCollection.fromFeature(featureLineString);
    }

}
