package uk.ac.ed.inf;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents the deliveries made in a day and outputs a delivery file.
 */

public record Deliveries(String orderNo, String orderOutcome, int costInPence) {

    /**
     * Outputs the delivery json file.
     * @param deliveries The list of deliveries to be outputted in the file.
     */
    public static void outputJsonDeliveries(ArrayList<Deliveries> deliveries) {
        try {
            String date = Server.getInstance().getDate();
            FileOutputStream file = new FileOutputStream("resultfiles/deliveries-" + date + ".json");
            BufferedOutputStream buff = new BufferedOutputStream(file);

            for(Deliveries delivery : deliveries) {
                String json = new ObjectMapper().writeValueAsString(delivery);
                buff.write(json.getBytes());
            }

            buff.close();
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
