package uk.ac.ed.inf;

import java.util.*;

public class Drone {
    /**
     * Represent the drone:
     * - its position
     * - battery left
     */

    public int battery;
    public LngLat start;
    public ArrayList<Order> order;
    public static final int FULL_BATTERY = 2000;
    public static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);


    public Drone(DayOrder order) {
        this.battery = FULL_BATTERY;
        this.start = APPLETON_TOWER;
        this.order = order.sortOrderByRestaurant();
    }

    public int batteryCost(ArrayList<LngLat> path) {
        return path.size()*2;
    }

    public boolean enoughBattery(ArrayList<LngLat> path) {
        int batteryCost = batteryCost(path);
        return battery >= batteryCost;
    }


    public ArrayList<LngLat> doTour(Restaurant[] restaurants, ArrayList<ArrayList<LngLat>> noFLyZones) {
        ArrayList<LngLat> completeTour = new ArrayList<>();
        ArrayList<Restaurant> arrayRestaurant = new ArrayList<>(Arrays.asList(restaurants));
        Restaurant.sortRestaurants(arrayRestaurant);

        for (Restaurant restaurant : arrayRestaurant) {
            // Compute path to restaurant
            LngLat coordinates = new LngLat(restaurant.getLng(), restaurant.getLat());
            ArrayList<LngLat> path = Path.getPathPoints(APPLETON_TOWER.toNode(), coordinates.toNode(), noFLyZones);
            ArrayList<LngLat> returnPath = Path.pathToStart(path);
            ArrayList<Order> copyOrder = new ArrayList<>(order);


            for (Order ord : copyOrder) {

                if (batteryCost(path) > battery) {
                    // Not enough battery so don't leave.
                    break;
                }

                if (ord.getOrderOutcome() == OrderOutcome.ValidButNotDelivered
                        && ord.restaurantOrdered(restaurants) == restaurant) {

                    completeTour.addAll(path);
                    completeTour.addAll(returnPath);
                    ord.setOrderOutcome(OrderOutcome.Delivered);

                    battery -= batteryCost(path);
                }
            }

        }

        System.out.println(battery);
        return completeTour;
    }

}

