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

    public int getDeliveryCost(Restaurant[] restaurants, String... pizzasOrdered) {
        int cost = 0;
        Restaurant restaurantOrdered = null;
        // finds the restaurant where the order is being made to
        for (Restaurant restaurant : restaurants) {
            Menu[] menu = restaurant.getMenu();
            for (Menu pizza : menu) {
                if (pizza.getName().equals(pizzasOrdered[0])) {
                    restaurantOrdered = restaurant;
                    break;
                }
            }
            if (restaurantOrdered != null) {
                break;
            }
        }

        assert restaurantOrdered != null;

        Menu[] menus = restaurantOrdered.getMenu();
        for (String s : pizzasOrdered) {
            for (Menu menu : menus) {
                if (menu.getName().equals(s)) {
                    cost += menu.getPriceInPence();
                }
            }
        }

        if (cost != 0) {
            cost +=100;
        }

        return cost;
    }

}
