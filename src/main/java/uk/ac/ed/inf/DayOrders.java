package uk.ac.ed.inf;

import java.util.*;

public class DayOrders {

    private final ArrayList<Order> dayOrders = new ArrayList<>();

    public ArrayList<Order> getDayOrders(ArrayList<Order> orders, Restaurant[] restaurants) {
        sortOrderByRestaurant(orders, restaurants);
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
    private void sortOrderByRestaurant(ArrayList<Order> orders, Restaurant[] restaurants) {
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
