package uk.ac.ed.inf;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents a movement of a drone.
 */
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
     * @param flightpath The list of moves to be outputted in the file.
     */
    public static void outputJsonFlightpath(ArrayList<Flightpath> flightpath) {
        try {
            String date = Server.getInstance().getDate();
            FileWriter file = new FileWriter("resultfiles/flightpath-" + date + ".json");
            JSONArray list = new JSONArray();

            for (Flightpath move : flightpath) {
                JSONObject obj = new JSONObject();
                obj.put("orderNo", move.orderNo);
                obj.put("fromLongitude", move.fromLongitude);
                obj.put("fromLatitude", move.fromLatitude);
                obj.put("angle", move.angle);
                obj.put("toLongitude", move.toLongitude);
                obj.put("toLatitude", move.toLatitude);
                obj.put("ticksSinceStartOfCalculation", move.ticksSinceStartOfCalculation);
                list.add(obj);
            }

            file.write(list.toJSONString());
            file.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
