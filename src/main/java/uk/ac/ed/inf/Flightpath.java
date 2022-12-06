package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
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
            FileOutputStream file = new FileOutputStream("resultfiles/flightpath-" + date + ".json");
            BufferedOutputStream buffer = new BufferedOutputStream(file);

            for(Flightpath move : flightpath) {
                String json = new ObjectMapper().writeValueAsString(move);
                buffer.write(json.getBytes());
            }

            buffer.close();
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
