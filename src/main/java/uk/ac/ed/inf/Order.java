package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Represents an order.
 */
public class Order {

    @JsonProperty("orderNo")
    private String orderNo;
    @JsonProperty("orderDate")
    private String orderDate;
    @JsonProperty("customer")
    private String customer;
    @JsonProperty("creditCardNumber")
    private String creditCardNumber;
    @JsonProperty("creditCardExpiry")
    private String creditCardExpiry;
    @JsonProperty("cvv")
    private String cvv;
    @JsonProperty("priceTotalInPence")
    private int priceTotalInPence;
    @JsonProperty("orderItems")
    private String[] orderItems;
    private OrderOutcome orderOutcome;

    /**
     * Default constructor.
     */
    Order(String orderNo, String orderDate, String customer, String creditCardNumber, String creditCardExpiry,
          String cvv, int priceTotalInPence, String[] orderItems) {
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.customer = customer;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiry = creditCardExpiry;
        this.cvv = cvv;
        this.priceTotalInPence = priceTotalInPence;
        this.orderItems = orderItems;
        orderOutcome = OrderOutcome.NotChecked;
    }

    Order() {

    }

    public Order[] getOrdersFromServer(URL serverBaseAddress, String date) {
        String endpoint = "orders/";
        String url = serverBaseAddress.toString() + endpoint + date;
        System.out.println(url);
        try {
            return new ObjectMapper().readValue(new URL(url), Order[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the restaurant from the corresponding order.
     * @param restaurants List of all restaurants.
     * @param pizzasOrdered List of pizzas from an order.
     * @return The restaurant that corresponds to the order.
     */
    public Restaurant restaurantOrdered(Restaurant[] restaurants) {
        Restaurant restaurantOrdered = null;

        // Finds the restaurant where the order is being made to.
        for (Restaurant restaurant : restaurants) {
            Menu[] menu = restaurant.getMenu();
            for (Menu pizza : menu) {
                if (pizza.getName().equals(orderItems[0])) {
                    restaurantOrdered = restaurant;
                    break;
                }
            }
            if (restaurantOrdered != null) {
                break;
            }
        }

        return restaurantOrdered;
    }

    public boolean arePizzasFromSameRestaurant(Restaurant[] restaurants, String... pizzasOrdered) {
        Restaurant restaurant = restaurantOrdered(restaurants);

        // Checks if all pizzas come from the same restaurant.
        int count = 0;
        for (String pizza : pizzasOrdered) {
            for (Menu dish : restaurant.getMenu()) {
                if (pizza.equals(dish.getName())) {
                    count++;
                    break;
                }
            }
        }

        return count == pizzasOrdered.length;
    }

    public LocalDate stringToDate(String string) {
        if (string.length() > 5) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy, MM, dd", Locale.ENGLISH);
            return LocalDate.parse(string, formatter);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM, yy", Locale.ENGLISH);
            return LocalDate.parse(string, formatter);
        }
    }

    /**
     * Calculates the cost in pence of having all the pizzas delivered by the drone, including the standard
     * delivery charge of 1 pound per delivery.
     * @param restaurantOrdered The list of participating restaurants.
     * @param pizzasOrdered Variable number of strings for the individual pizzas ordered.
     * @return The cost of the pizzas being delivered by the drone in pence.
     */
    public int getDeliveryCost(Restaurant restaurantOrdered) {
        int cost = 100;
        // Calculates cost.
        Menu[] menus = restaurantOrdered.getMenu();
        for (String pizza : orderItems) {
            for (Menu menu : menus) {
                if (menu.getName().equals(pizza)) {
                    cost += menu.getPriceInPence();
                    break;
                }
            }
        }
        return cost;
    }

    public String[] getOrderItems() {
        return orderItems;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public OrderOutcome getOrderOutcome() {
        return orderOutcome;
    }

    public void setOrderOutcome(OrderOutcome orderOutcome) {
        this.orderOutcome = orderOutcome;
    }

    public String getCvv() {
        return cvv;
    }

    public int getPriceTotalInPence() {
        return priceTotalInPence;
    }

    public String getCreditCardExpiry() {
        return creditCardExpiry;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public String getOrderNo() {
        return orderNo;
    }
}
