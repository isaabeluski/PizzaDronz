package uk.ac.ed.inf;

import java.net.MalformedURLException;
import java.util.*;

public class Drone {
    /**
     * Represent the drone:
     * - its position
     * - battery left
     */

    public int battery;
    public LngLat start;
    public ArrayList<Order> currentOrder;
    public static final int FULL_BATTERY = 2000;
    public static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);


    public Drone(ArrayList<Order> currentOrder) throws MalformedURLException {
        this.battery = FULL_BATTERY;
        this.start = APPLETON_TOWER;
        this.currentOrder = currentOrder;
    }

    /*
    public Restaurant getClosestRestaurant(ArrayList<Restaurant> restaurants) {
        int minimumMoves = Integer.MAX_VALUE;
        Restaurant closestRestaurant = new Restaurant();
        for (Restaurant restaurant : restaurants) {
            LngLat coordinates = new LngLat(restaurant.getLat(), restaurant.getLng());
            Node destinationNode = coordinates.toNode();
            Node startNode = start.toNode();
            ArrayList<LngLat> path = startNode.findPath(destinationNode);
            int movesToRestaurant = path.size();

            if (movesToRestaurant < minimumMoves) {
                minimumMoves = movesToRestaurant;
                closestRestaurant = restaurant;
            }
        }
        return closestRestaurant;
    }
     */

    public void doTour(Restaurant[] restaurants, ArrayList<ArrayList<LngLat>> noFLyZones) {
        // Try to do it for just one order
        //Order order = currentOrder.get(2);
        // Restaurant restaurant = order.restaurantOrdered(restaurants);
        // Restaurant restaurant = restaurants[0];
        // System.out.println(restaurant.getName());
        // LngLat coordinatesRestaurant = new LngLat(restaurant.getLng(), restaurant.getLat());

        for (Order order : currentOrder) {
            Restaurant restaurant = order.restaurantOrdered(restaurants);
            System.out.println(restaurant.getName());
            LngLat coordinatesRestaurant = new LngLat(restaurant.getLng(), restaurant.getLat());
            ArrayList<LngLat> path = start.toNode().findPath(coordinatesRestaurant.toNode(), noFLyZones);
            System.out.println(path);
        }

        // return start.toNode().findPath(coordinatesRestaurant.toNode(), noFLyZones);

        /*
        HashMap<String, ArrayList<LngLat>> allPaths = new HashMap<>();
        Restaurant restaurant = restaurants[0];
        LngLat coordinatesRestaurant = new LngLat(restaurant.getLng(), restaurant.getLat());
        ArrayList<LngLat> path = start.toNode().findPath(coordinatesRestaurant.toNode(), noFLyZones);
        allPaths.put(restaurant.getName(), path);

        return allPaths;

         */

        /*
        HashMap<String, ArrayList<LngLat>> allPaths = new HashMap<>();

        for (Restaurant restaurant : restaurants) {
            LngLat coordinatesRestaurant = new LngLat(restaurant.getLng(), restaurant.getLat());
            String name = restaurant.getName();
            ArrayList<LngLat> path = start.toNode().findPath(coordinatesRestaurant.toNode());
            allPaths.put(name, path);
        }
         */


        //ArrayList<Restaurant> allRestaurants = new ArrayList<>(List.of(restaurants));
        //ArrayList<LngLat> tour = new ArrayList<>();
        //HashMap<Order, Restaurant> orders = new HashMap<>();
        //boolean enoughBattery = true;
        //for (Order a : this.currentOrder) {
        //    orders.put(a, a.restaurantOrdered(restaurants, a.getOrderItems()));
        //}
        //while (!allRestaurants.isEmpty() || enoughBattery) {
        //    Restaurant currentRestaurant = getClosestRestaurant(allRestaurants);
        //    LngLat coordinatesRestaurant = new LngLat(currentRestaurant.getLat(), currentRestaurant.getLng());
        //    ArrayList<LngLat> path = start.toNode().findPath(coordinatesRestaurant.toNode());
        //    for (Map.Entry<Order, Restaurant> oneOrder : orders.entrySet()) {
        //        if (oneOrder.getValue()== currentRestaurant) {
        //            // If another order cannot be completed, stop!
        //            if (path.size()*2 < battery) {
        //                tour.addAll(path);
        //                Collections.reverse(path);
        //                tour.addAll(path);
        //            } else {
        //                enoughBattery = false;
        //                break;
        //            }
        //        }
        //    }
        //    allRestaurants.remove(currentRestaurant);
        //}
        //return tour;
    }


}
