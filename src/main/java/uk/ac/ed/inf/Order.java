package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents an order.
 */
public class Order {

    private final String orderNo;
    private final String orderDate;
    private final String creditCardNumber;
    private final String creditCardExpiry;
    private final String cvv;
    private final int priceTotalInPence;
    private final String[] orderItems;
    private OrderOutcome orderOutcome;
    private static final String endPoint = "orders";


    /**
     * Default constructor.
     */
    Order(@JsonProperty("orderNo") String orderNo, @JsonProperty("orderDate") String orderDate,
          @JsonProperty("customer") String customer, @JsonProperty("creditCardNumber") String creditCardNumber,
          @JsonProperty("creditCardExpiry") String creditCardExpiry, @JsonProperty("cvv") String cvv,
          @JsonProperty("priceTotalInPence") int priceTotalInPence, @JsonProperty("orderItems") String[] orderItems) {
        this.orderNo = orderNo;
        this.orderDate = orderDate;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpiry = creditCardExpiry;
        this.cvv = cvv;
        this.priceTotalInPence = priceTotalInPence;
        this.orderItems = orderItems;
        orderOutcome = OrderOutcome.NotChecked;
    }


    /**
     * Gets orders from REST server.
     * @return A list of all orders for a specific date.
     */
    public static ArrayList<Order> getOrders() {
        try {
            String baseUrl = Server.getInstance().baseUrl;
            String date = Server.getInstance().getDate();

            String url = baseUrl + endPoint + "/" + date;

            Order[] orders = new ObjectMapper().readValue(new URL(url), Order[].class);
            return new ArrayList<>(List.of(orders));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the restaurant from the corresponding order.
     * @param restaurants List of all restaurants.
     * @return The restaurant that corresponds to the order.
     */
    public Restaurant restaurantOrdered(Restaurant[] restaurants) {
        Restaurant restaurantOrdered = null;

        // Finds the restaurant where the order is being made to.
        for (Restaurant restaurant : restaurants) {
            if (restaurant.menuCost().containsKey(orderItems[0])) {
                restaurantOrdered = restaurant;
            }
            if (restaurantOrdered != null) {
                break;
            }
        }
        return restaurantOrdered;
    }

    /**
     * Checks whether the order items come from the same restaurant.
     * @param restaurants List of all restaurants.
     * @return True if the order is valid, false otherwise.
     */
    public boolean arePizzasFromSameRestaurant(Restaurant[] restaurants, String... pizzasOrdered) {
        Restaurant restaurant = restaurantOrdered(restaurants);
        for (String pizza : pizzasOrdered) {
            if (!restaurant.menuCost().containsKey(pizza)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Calculates the cost in pence of having all the pizzas delivered by the drone, including the standard
     * delivery charge of 1 pound per delivery.
     * @param restaurantOrdered The restaurant where the order is being made to.
     * @return The cost of the pizzas being delivered by the drone in pence.
     */
    public int getDeliveryCost(Restaurant restaurantOrdered) {
        int cost = 100;
        HashMap<String, Integer> menu = restaurantOrdered.menuCost();
        for (String pizza : orderItems) {
            cost += menu.get(pizza);
        }
        // Calculates cost.
        return cost;
    }


    // GETTERS AND SETTERS.
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
