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

    // Starting point representing Appleton Tower in this case.
    public static final LngLat STARTING_POINT = new LngLat(-3.186874, 55.944494);
    public static long startTime;


    public Drone(DayOrder order) {
        this.battery = FULL_BATTERY;
        this.start = STARTING_POINT;
        this.currentPos = STARTING_POINT;
        this.order = order.sortOrderByRestaurant();
    }

    public int batteryCost(ArrayList<LngLat> path1, ArrayList<LngLat> path2) {
        return path1.size() + path2.size();
    }

    public void hover(Order order, long ticks) {
        LngLat nextPos = currentPos.nextPosition(null);
        flightpath.add(new Flightpath(
                order.getOrderNo(),
                currentPos,
                null,
                nextPos,
                ticks));
        this.battery -= 1;
    }


    public ArrayList<LngLat> makeDeliveries(Restaurant[] restaurants) {
        ArrayList<LngLat> completeTour = new ArrayList<>();
        ArrayList<Deliveries> deliveries = new ArrayList<>();
        startTime = System.nanoTime();

        for (Order ord : order) {

            Restaurant correspondingRestaurant = ord.restaurantOrdered(restaurants);
            LngLat restaurantPos = new LngLat(correspondingRestaurant.getLng(), correspondingRestaurant.getLat());
            var pathToRestaurant = Path.getPathPoints(start, restaurantPos);
            var node1 = Path.getPathNodes(start.toNode(), restaurantPos.toNode());
            var returnPath = Path.getPathPoints(restaurantPos, start);
            var node2 = Path.getPathNodes(restaurantPos.toNode(), start.toNode());

            if (batteryCost(pathToRestaurant, returnPath) > battery) {
                // Not enough battery so don't leave.
                break;
            }

            if (ord.getOrderOutcome() == OrderOutcome.ValidButNotDelivered) {
                completeTour.addAll(pathToRestaurant);
                flightpath.addAll(Path.getMoves(ord, node1));
                //hover(ord, System.nanoTime() - startTime);

                completeTour.addAll(returnPath);
                flightpath.addAll(Path.getMoves(ord, node2));
                //hover(ord, System.nanoTime() - startTime);
                ord.setOrderOutcome(OrderOutcome.Delivered);
                battery -= batteryCost(pathToRestaurant, returnPath);

            }

        }

        for (Order ord : order) {
            Deliveries delivery = new Deliveries(ord.getOrderNo(), ord.getOrderOutcome(), ord.getPriceTotalInPence());
            deliveries.add(delivery);
        }

        Deliveries.outputJsonDeliveries("01", "01", "2023", deliveries);
        Flightpath.outputJsonFlightpath("01", "01", "2023", flightpath);

        System.out.println("Number of moves: " + flightpath.size());


        // TODO: Borrar esto después.
        System.out.println("Battery remaining: " + battery);
        int count = 0;
        for (Order ord : order) {
            if (ord.getOrderOutcome() == OrderOutcome.Delivered) {
                count++;
            }
        }

        System.out.println("Number of orders delivered: " + count);
        System.out.println("Number of total orders: " + order.size());
        //System.out.println(flightpath.get(0).angle);
        return completeTour;
    }
}

