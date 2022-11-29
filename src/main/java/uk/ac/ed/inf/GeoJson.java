package uk.ac.ed.inf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.FeatureCollection;


import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeoJson {


    public void outputGeoJsonFolder(String day, String month, String year, ArrayList<LngLat> path) {
        try{

            FileWriter file = new FileWriter("drone-"+day +"-" + month + "-" + year +".geojson");
            file.write(toFeatureCollection(path).toJson());
            file.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private FeatureCollection toFeatureCollection(ArrayList<LngLat> path){
        ArrayList<Point> points = new ArrayList<>();

        for(LngLat move: path){
            points.add(Point.fromLngLat(move.lng(), move.lat()));
        }

        var featureLineString =  Feature.fromGeometry(LineString.fromLngLats(points));
        return FeatureCollection.fromFeature(featureLineString);
    }

}
