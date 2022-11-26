package uk.ac.ed.inf;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeoJson {

    public List<List<Double>> coordinates(ArrayList<LngLat> coordinates) {
        List<List<Double>> newCoordinates = new ArrayList<>();
        int i = 0;
        for (LngLat coord : coordinates) {
            List<Double> xy = new ArrayList<>();
            xy.add(coord.lng());
            xy.add(coord.lat());
            newCoordinates.add(xy);
        }
        return newCoordinates;
    }

    public void createJsonFile(List<List<Double>> coordinates) {
        JSONObject line = new JSONObject();
        line.put("TYPE", "LineString");
        line.put("coordinates", coordinates);

        JSONArray object = new JSONArray();
        object.put(line);

        try (FileWriter file = new FileWriter("caca.json")) {
            //We can write any JSONArray or JSONObject instance to the file
            file.write(object.toString());
            file.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
