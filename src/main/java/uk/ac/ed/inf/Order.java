package uk.ac.ed.inf;

import java.util.ArrayList;
import java.util.Date;

public class Order {

    public String orderNo;
    public Date orderDate;
    public String customer;
    public String creditCardNumber;
    public Date creditCardExpiry;
    public String ccv;
    public int priceTotalInPence;
    public String[] orderItems;

    public int getDeliveryCost(ArrayList<Restaurant> restaurants, String... pizzasOrdered) {
        int cost = 0;
        Restaurant restaurantOrdered = null;
        // finds the restaurant where the order is being made to
        for (Restaurant restaurant : restaurants) {
            for (Menu menu : restaurant.getMenu()) {
                if (menu.getName().equals(pizzasOrdered[0])) {
                    restaurantOrdered = restaurant;
                    break;
                }
            }
        }

        Menu[] menus = restaurantOrdered.getMenu();
        for (int i = 0; i < pizzasOrdered.length - 1; i++) {
            for (Menu menu : menus) {
                if (menu.getName().equals(pizzasOrdered[i])) {
                    cost += menu.getPriceInPence();
                }
            }
        }

        return cost;
    }

}
