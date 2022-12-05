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

    private final String orderNo;
    private final Double fromLongitude;
    private final Double fromLatitude;
    private final Double angle;
    private final Double toLongitude;
    private final Double toLatitude;
    private final long ticksSinceStartOfCalculation;

    /**
     * Constructor for Flightpath.
     * @param orderNo The order number.
     * @param from The starting point of the drone's move.
     * @param angle The angle the drone's move takes.
     * @param to The end point of the drone's move.
     * @param ticksSinceStartOfCalculation The number of ticks since the start of the calculation.
     */
    public Flightpath(String orderNo, LngLat from, Double angle, LngLat to, long ticksSinceStartOfCalculation) {
        this.orderNo = orderNo;
        this.fromLongitude = from.lng();
        this.fromLatitude = from.lat();
        this.angle = angle;
        this.toLongitude = to.lng();
        this.toLatitude = to.lat();
        this.ticksSinceStartOfCalculation = ticksSinceStartOfCalculation;
    }

    /**
     * Outputs the flightpath file.
     * @param day The day of the delivery.
     * @param month The month of the delivery.
     * @param year The year of the delivery.
     * @param flightpaths The list of flightpaths to be outputted in the file.
     */
    public static void outputJsonFlightpath(ArrayList<Flightpath> flightpaths) {
        try {
            String date = Server.getInstance().getDate();
            FileWriter file = new FileWriter("resultfiles/flightpath-" + date + ".json");
            JSONArray list = new JSONArray();
            for (Flightpath flightpath : flightpaths) {
                JSONObject obj = new JSONObject();
                obj.put("orderNo", flightpath.orderNo);
                obj.put("fromLongitude", flightpath.fromLongitude);
                obj.put("fromLatitude", flightpath.fromLatitude);
                obj.put("angle", flightpath.angle);
                obj.put("toLongitude", flightpath.toLongitude);
                obj.put("toLatitude", flightpath.toLatitude);
                obj.put("ticksSinceStartOfCalculation", flightpath.ticksSinceStartOfCalculation);
                list.add(obj);
            }
            //Gson gson = new GsonBuilder().setPrettyPrinting().create();
            //String json = gson.toJson(list);
            file.write(list.toJSONString());

            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
