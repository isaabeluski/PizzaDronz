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
    public static final LngLat APPLETON_TOWER = new LngLat(-3.186874, 55.944494);

    public Drone(DayOrder order) {
        this.battery = FULL_BATTERY;
        this.start = APPLETON_TOWER;
        this.order = order.sortOrderByRestaurant();
    }

    public int batteryCost(ArrayList<LngLat> path) {
        return path.size();
    }

    public boolean enoughBattery(ArrayList<LngLat> path) {
        int batteryCost = batteryCost(path);
        return battery >= batteryCost;
    }

    public void move(LngLat from, LngLat to, Order order) {
       new Flightpath(
               order.getOrderNo(),
               from.lng(),
               from.lat(),
               from.getAngleFromLine(to),
               to.lng(),
               to.lat()
       );
    }

    public HashMap<Restaurant, ArrayList<LngLat>> allPaths(Restaurant[] restaurants, Order order) {
        HashMap<Restaurant, ArrayList<LngLat>> paths = new HashMap<>();
        for (Restaurant restaurant : restaurants) {
            LngLat restaurantLngLat = new LngLat(restaurant.getLng(), restaurant.getLat());
            paths.put(restaurant, Path.totalPath(start.toNode(), restaurantLngLat.toNode(), order));
        }
        return paths;
    }


    public ArrayList<LngLat> doTour(Restaurant[] restaurants) {
        ArrayList<LngLat> completeTour = new ArrayList<>();
        ArrayList<Restaurant> arrayRestaurant = new ArrayList<>(Arrays.asList(restaurants));
        Restaurant.sortRestaurants(arrayRestaurant);


        for (Order ord : order) {

            Restaurant correspondingRestaurant = ord.restaurantOrdered(restaurants);
            LngLat restaurantCoordinates = new LngLat(correspondingRestaurant.getLng(), correspondingRestaurant.getLat());
            ArrayList<LngLat> path = Path.totalPath(APPLETON_TOWER.toNode(), restaurantCoordinates.toNode(), ord);

            if (batteryCost(path) > battery) {
                // Not enough battery so don't leave.
                break;
            }

            if (ord.getOrderOutcome() == OrderOutcome.ValidButNotDelivered) {
                completeTour.addAll(path);
                flightpath.addAll(Path.getFlightpath());
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
        return completeTour;
    }

    public ArrayList<Flightpath> getFlightpath() {
        return flightpath;
    }
}

