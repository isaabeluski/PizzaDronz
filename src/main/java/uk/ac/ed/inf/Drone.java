package uk.ac.ed.inf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

/**
 * Represents a drone and makes delivery possible.
 */
public class Drone {

    private int battery;
    private final LngLat start;
    private final OrderAdministration order;
    private final ArrayList<Flightpath> flightpath = new ArrayList<>();
    private static final int FULL_BATTERY = 2000;

    /**
     * Represents where the drone takes off from - in this case Appleton Tower.
     */
    public static final LngLat STARTING_POINT = new LngLat(-3.186874, 55.944494);

    /**
     * Used to keep track of the time one movement takes.
     */
    public static long startTime;


    public Drone(OrderAdministration order) {
        this.battery = FULL_BATTERY;
        this.start = STARTING_POINT;
        this.order = order;
    }

    /**
     * Calculates the battery used when going to a restaurant and coming back.
     * @param path1 The path to the restaurant.
     * @param path2 The path back to the start.
     * @return The battery used.
     */
    public int batteryCost(ArrayList<LngLat> path1, ArrayList<LngLat> path2) {
        return path1.size() + path2.size();
    }


    /**
     * Main method that makes the drone deliver the orders of a specific day. It goes through all orders of a day
     * and calculates the best path to the restaurant and back to the start. It takes into account the battery of the
     * drone, since it will not take off if there is not enough battery to go to the restaurant and back.
     * @param restaurants The list of restaurants.
     * @return A list with all the points the drone can go through in a day.
     */
    public ArrayList<LngLat> makeDeliveries(Restaurant[] restaurants) {

        ArrayList<LngLat> completeTour = new ArrayList<>();
        ArrayList<Deliveries> deliveries = new ArrayList<>();
        startTime = System.nanoTime();

        // Goes through the sorted order list, based on the restaurant with the shortest path.
        for (Order ord : order.getValidatedOrders()) {

            Restaurant correspondingRestaurant = ord.restaurantOrdered(restaurants);
            LngLat restaurantPos = new LngLat(correspondingRestaurant.getLng(), correspondingRestaurant.getLat());

            // Only try to deliver orders that are valid and have not been delivered yet.
            if (ord.getOrderOutcome() == OrderOutcome.ValidButNotDelivered) {

                var pathToRestaurant = new Path(start, restaurantPos);
                var pathToRestaurantList = pathToRestaurant.getPathInLngLat();

                var returnPath = new Path(restaurantPos, start);
                var returnPathList = returnPath.getPathInLngLat();

                if (batteryCost(pathToRestaurantList, returnPathList) > battery) {
                    break;
                }

                // Adds path to the restaurant.
                completeTour.addAll(pathToRestaurantList);
                flightpath.addAll(pathToRestaurant.getMoves(ord));

                // Adds path back to the start.
                completeTour.addAll(returnPathList);
                flightpath.addAll(returnPath.getMoves(ord));
                ord.setOrderOutcome(OrderOutcome.Delivered);

                battery -= batteryCost(pathToRestaurantList, returnPathList);

            }

        }

        for (Order ord : order.getValidatedOrders()) {
            Deliveries delivery = new Deliveries(ord.getOrderNo(), ord.getOrderOutcome(), ord.getPriceTotalInPence());
            deliveries.add(delivery);
        }

        Deliveries.outputJsonDeliveries(deliveries);
        Flightpath.outputJsonFlightpath(flightpath);
        outputGeoJson(completeTour);

        // TODO: Borrar esto después.
        System.out.println("Number of moves: " + flightpath.size());
        System.out.println("Battery remaining: " + battery);
        int count = 0;
        for (Order ord : order.getValidatedOrders()) {
            if (ord.getOrderOutcome() == OrderOutcome.Delivered) {
                count++;
            }
        }
        System.out.println("Number of orders delivered: " + count);
        System.out.println("Number of total orders: " + order.getValidatedOrders().size());

        return completeTour;
    }

    /**
     * Converts a list of LngLat to a FeatureCollection.
     * @param path The path the drone takes.
     * @return The FeatureCollection.
     */
    private FeatureCollection lnglatToFC(ArrayList<LngLat> path){
        ArrayList<Point> points = new ArrayList<>();

        for(LngLat move: path){
            points.add(Point.fromLngLat(move.lng(), move.lat()));
        }

        Feature featureLineString =  Feature.fromGeometry(LineString.fromLngLats(points));
        return FeatureCollection.fromFeature(featureLineString);
    }

    /**
     * Creates a GeoJson file from a list of points.
     * @param path  The path the drone takes.
     */
    public void outputGeoJson(ArrayList<LngLat> path) {
        try {
            String date = Server.getInstance().getDate();
            FileWriter file = new FileWriter("resultfiles/drone-" + date + ".geojson");
            file.write(lnglatToFC(path).toJson());
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

