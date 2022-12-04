package uk.ac.ed.inf;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represents the deliveries made in a day and outputs a delivery file.
 */

public class Deliveries {

    private final String orderNo;
    private final String orderOutcome;
    private final int costInPence;

    public Deliveries(String orderNo, OrderOutcome orderOutcome, int costInPence) {
        this.orderNo = orderNo;
        this.orderOutcome = orderOutcome.name();
        this.costInPence = costInPence;
    }

    /**
     * Outputs the delivery file.
     * @param day The day of the delivery.
     * @param month The month of the delivery.
     * @param year The year of the delivery.
     * @param deliveries The list of deliveries to be outputted in the file.
     */
    public static void outputJsonDeliveries(String day, String month, String year, ArrayList<Deliveries> deliveries) {
        try {

            FileWriter file = new FileWriter("resultfiles/deliveries-" + year + "-" + month + "-" + day + ".json");
            JSONArray list = new JSONArray();
            for (Deliveries delivery : deliveries) {
                JSONObject obj = new JSONObject();
                obj.put("orderNo", delivery.orderNo);
                obj.put("orderOutcome", delivery.orderOutcome);
                obj.put("costInPence", delivery.costInPence);
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
