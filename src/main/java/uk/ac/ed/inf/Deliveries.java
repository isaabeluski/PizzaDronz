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

    /**
     * Constructor
     * @param orderNo The order number.
     * @param orderOutcome The outcome of the order.
     * @param costInPence The cost of the order.
     */
    public Deliveries(String orderNo, OrderOutcome orderOutcome, int costInPence) {
        this.orderNo = orderNo;
        this.orderOutcome = orderOutcome.name();
        this.costInPence = costInPence;
    }

    /**
     * Outputs the delivery json file.
     * @param deliveries The list of deliveries to be outputted in the file.
     */
    public static void outputJsonDeliveries(ArrayList<Deliveries> deliveries) {
        try {
            String date = Server.getInstance().getDate();
            FileWriter file = new FileWriter("resultfiles/deliveries-" + date + ".json");
            JSONArray list = new JSONArray();

            for (Deliveries delivery : deliveries) {
                JSONObject obj = new JSONObject();
                obj.put("orderNo", delivery.orderNo);
                obj.put("orderOutcome", delivery.orderOutcome);
                obj.put("costInPence", delivery.costInPence);
                list.add(obj);
            }

            file.write(list.toJSONString());
            file.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
