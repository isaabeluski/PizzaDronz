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
    private ArrayList<Flightpath> flightpath = new ArrayList<>();
    public static final int FULL_BATTERY = 2000;
    public LngLat currentPos;
    public static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);


    public Drone(DayOrder order) {
        this.battery = FULL_BATTERY;
        this.start = APPLETON_TOWER;
        this.currentPos = APPLETON_TOWER;
        this.order = order.sortOrderByRestaurant();
    }

    public int batteryCost(ArrayList<LngLat> path) {
        return path.size();
    }

    public boolean enoughBattery(ArrayList<LngLat> path) {
        int batteryCost = batteryCost(path);
        return battery >= batteryCost;
    }

    public void hover(Order order) {
        LngLat nextPos = currentPos.nextPosition(null);
        flightpath.add(new Flightpath(
                order.getOrderNo(),
                currentPos,
                null,
                nextPos));
        this.battery -= 1;
    }


    public ArrayList<LngLat> makeDeliveries(Restaurant[] restaurants) {
        HashMap<Restaurant, ArrayList<LngLat>> allPaths = new Path(restaurants).getAllPaths();
        ArrayList<LngLat> completeTour = new ArrayList<>();

        for (Order ord : order) {

            Restaurant correspondingRestaurant = ord.restaurantOrdered(restaurants);
            var path = allPaths.get(correspondingRestaurant);

            if (batteryCost(path) > battery) {
                // Not enough battery so don't leave.
                break;
            }

            if (ord.getOrderOutcome() == OrderOutcome.ValidButNotDelivered) {
                completeTour.addAll(path);
                ord.setOrderOutcome(OrderOutcome.Delivered);
                battery -= batteryCost(path);

            }
        }


        // TODO: Borrar esto después.
        System.out.println("Battery remaining: " + battery);
        int count = 0;
        for (Order ord : order) {
            if (ord.getOrderOutcome() == OrderOutcome.Delivered) {
                System.out.println("Order " + ord.restaurantOrdered(restaurants).getName() + " delivered");
                count++;
            }
            if (ord.getOrderOutcome() == OrderOutcome.ValidButNotDelivered) {
                System.out.println("Order " + ord.restaurantOrdered(restaurants).getName() + " not delivered");
            }
        }

        System.out.println("Number of orders delivered: " + count);
        //System.out.println(flightpath.get(0).angle);
        return completeTour;
    }

    public ArrayList<Flightpath> getFlightpath() {
        return flightpath;
    }
}

