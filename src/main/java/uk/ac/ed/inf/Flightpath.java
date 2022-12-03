package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

    public Flightpath(String orderNo, LngLat from, Double angle, LngLat to) {
        this.orderNo = orderNo;
        this.fromLongitude = from.lng();
        this.fromLatitude = from.lat();
        this.angle = angle;
        this.toLongitude = to.lng();
        this.toLatitude = to.lat();
        //this.ticksSinceStartOfCalculation = ticksSinceStartOfCalculation;
    }

    public static void outputJsonFlightpath(String day, String month, String year, ArrayList<Flightpath> flightpaths) {
        try {

            FileWriter file = new FileWriter("flightpath-" + year + "-" + month + "-" + day + ".json");
            ObjectMapper Obj = new ObjectMapper();
            JSONArray list = new JSONArray();
            for (Flightpath flightpath : flightpaths) {
                JSONObject obj = new JSONObject();
                obj.put("orderNo", flightpath.orderNo);
                obj.put("fromLongitude", flightpath.fromLongitude);
                obj.put("fromLatitude", flightpath.fromLatitude);
                obj.put("angle", flightpath.angle);
                obj.put("toLongitude", flightpath.toLongitude);
                obj.put("toLatitude", flightpath.toLatitude);
                list.add(obj);
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(list);
            file.write(json);

            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
