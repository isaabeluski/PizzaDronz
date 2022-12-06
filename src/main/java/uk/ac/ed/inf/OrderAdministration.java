package uk.ac.ed.inf;

import java.util.*;

public class OrderAdministration {


    private final ArrayList<Order> dayOrders = new ArrayList<>();
    private final Restaurant[] restaurants = Restaurant.getRestaurants();


    public ArrayList<Order> getDayOrders(ArrayList<Order> orders) {
        sortOrderByRestaurant(orders);
        for (Order ord : dayOrders) {
            ord.setOrderOutcomes(restaurants);
        }
        return dayOrders;
    }

    /**
     * Sorts the ArrayList of orders by their restaurants, where the restaurant closer to the start goes
     * first.
     * @return An ArrayList of Orders, where orders from the closest restaurant are first.
     */
    private void sortOrderByRestaurant(ArrayList<Order> orders) {
        ArrayList<Restaurant> arrayRestaurant = new ArrayList<>(Arrays.asList(restaurants));
        Restaurant.sortRestaurants(arrayRestaurant);
        HashMap<Restaurant, ArrayList<Order>> map = new HashMap<>();

        for (Order ord : orders) {
            Restaurant restaurant = ord.restaurantOrdered(restaurants);
            if (map.containsKey(restaurant)) {
                map.get(restaurant).add(ord);
            }
            else {
                ArrayList<Order> newOrder = new ArrayList<>();
                newOrder.add(ord);
                map.put(restaurant, newOrder);
            }
        }

        for (Restaurant rest : arrayRestaurant) {
            if (map.containsKey(rest)) {
                dayOrders.addAll(map.get(rest));
            }
        }
    }

}
