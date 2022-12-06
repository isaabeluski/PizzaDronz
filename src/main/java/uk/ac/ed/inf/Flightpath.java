package uk.ac.ed.inf;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents a movement of a drone.
 */
public record Flightpath(String orderNo,
                         Double fromLongitude,
                         Double fromLatitude,
                         Double angle,
                         Double toLongitude,
                         Double toLatitude,
                         long ticksSinceStartOfCalculation) {


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
