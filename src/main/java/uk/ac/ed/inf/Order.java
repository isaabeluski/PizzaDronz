package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.GenericValidator;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a single order.
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
            String baseUrl = Server.getInstance().getBaseUrl();
            String date = Server.getInstance().getDate();

            String url = baseUrl + endPoint + "/" + date;
            Order[] orders = new ObjectMapper().readValue(new URL(url), Order[].class);

            return new ArrayList<>(List.of(orders));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setOrderOutcomes(Restaurant[] restaurants) {

            // Check if the number of pizzas ordered is valid.
            if (orderItems.length < 1 || orderItems.length > 4) {
                orderOutcome = OrderOutcome.InvalidPizzaCount;
            }
            // Check if the pizzas ordered are from the same restaurant.
            else if (!arePizzasFromSameRestaurant(restaurants, orderItems)) {
                orderOutcome = OrderOutcome.InvalidPizzaCombinationMultipleSuppliers;
            }
            // Check if the total price of the order is right.
            else if (getDeliveryCost(restaurantOrdered(restaurants)) != priceTotalInPence) {
                orderOutcome = OrderOutcome.InvalidTotal;
            } else {
                // Otherwise, the order is valid.
                orderOutcome = OrderOutcome.ValidButNotDelivered;
            }

            // Check if the credit card information is valid.
            validCreditCard();
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

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9]+");
    }

    /**
     * Checks whether the credit card used in an order is valid.
     * @return True if the credit card is valid, false otherwise.
     */
    public void validCreditCard() {
        // Check cardExpiry
        if (!checkExpiryDate()) {
            orderOutcome = OrderOutcome.InvalidExpiryDate;
        }
        // Check cvv
        else if (cvv.length() != 3 || !isNumeric(cvv)) {
            orderOutcome = OrderOutcome.InvalidCvv;
        }
        // Check credit card number
        else if (!isNumeric(creditCardNumber) || creditCardNumber.length() != 16) {
            orderOutcome = OrderOutcome.InvalidCardNumber;
        }
        // Check if it is either Visa or Mastercard from IINs
        else if (!validIIN()) {
            orderOutcome = OrderOutcome.InvalidCardNumber;
        }
        else if (!luhnAlgorithm()) {
            orderOutcome = OrderOutcome.InvalidCardNumber;
        }
    }

    /**
     * Checks if the expiry date of the credit card is valid. As a result, its format must be MM/YY and the card
     * must have not expired by the order date.
     * @return True if the expiry date is valid, false otherwise.
     */
    public boolean checkExpiryDate() {

        SimpleDateFormat formatterExpiry = new SimpleDateFormat("MM/yy");
        SimpleDateFormat formatterOrderDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat getYear = new SimpleDateFormat("yyyy");

        // Checks that the format of the expiry date and the order date is correct.
        if (!GenericValidator.isDate(creditCardExpiry, formatterExpiry.toPattern(), true) ||
                !GenericValidator.isDate(orderDate, formatterOrderDate.toPattern(), true)) {
            return false;
        }

        try {
            String month = creditCardExpiry.substring(0, 2);
            if (month.startsWith("0")) {
                month = month.substring(1);
            }

            // Turns the expiry year into the format yyyy.
            String expiryYear = getYear.format(formatterExpiry.parse(creditCardExpiry));
            month = Integer.parseInt(month) + 1 + "";
            String newExpiryDate = expiryYear + "-" + month + "-01";


            Date expiry = formatterOrderDate.parse(newExpiryDate);
            Date dateOrder = formatterOrderDate.parse(orderDate);

            if (expiry.before(dateOrder) || expiry.equals(dateOrder)) {
                return false;
            }
        } catch (ParseException e) {
            return false;}

        return true;
    }

    /**
     * Checks if the credit card number is valid using the Luhn algorithm.
     * @return True if the credit card number is valid, false otherwise.
     */
    public boolean luhnAlgorithm() {
        int cardLength = creditCardNumber.length();

        int totalSum = 0;
        boolean skip = true;
        for (int i = cardLength - 1; i >= 0; i--)
        {

            // Converts char to its integer value.
            int digit = creditCardNumber.charAt(i) - '0';

            if (!skip) {
                digit *= 2;
            }

            if (digit > 9) {
                int firstDigit = Integer.parseInt(Integer.toString(digit).substring(0, 1));
                int secondDigit = Integer.parseInt(Integer.toString(digit).substring(1, 2));
                digit = firstDigit + secondDigit;
            }

            totalSum += digit;

            skip = !skip;
        }
        return (totalSum % 10 == 0);
    }

    /**
     * Check if the credit card number is valid based on the valid IINs for Visa and Mastercard.
+     * @return True if the credit card IIN is valid.
     */
    public boolean validIIN() {
        try {
            long firstDigit = Long.parseLong(creditCardNumber.substring(0, 1));
            long fourDigits = Long.parseLong(creditCardNumber.substring(0, 4));
            long twoDigits = Long.parseLong(creditCardNumber.substring(0, 2));

            // check for VISA
            if (firstDigit == 4) {
                return true;
            }

            // check for mastercard
            for (int i = 51; i <= 55 ; i++) {
                if (twoDigits == i) {
                    return true;
                }
            }

            for (int i = 2221; i <= 2720 ; i++) {
                if (fourDigits == i) {
                    return true;
                }
            }

        }
        catch (NumberFormatException e) {
            orderOutcome = OrderOutcome.InvalidCardNumber;
            return false;
        }

        return false;
    }

    // GETTERS AND SETTERS.

    public OrderOutcome getOrderOutcome() {
        return orderOutcome;
    }

    public void setOrderOutcome(OrderOutcome orderOutcome) {
        this.orderOutcome = orderOutcome;
    }

    public int getPriceTotalInPence() {
        return priceTotalInPence;
    }

    public String getOrderNo() {
        return orderNo;
    }
}
