package uk.ac.ed.inf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrderAdministration {

    String date;
    ArrayList<Order> sortedOrders = new ArrayList<>();
    ArrayList<Order> validatedOrders = new ArrayList<>();
    Restaurant[] restaurants;

    OrderAdministration(ArrayList<Order> orders, Restaurant[] restaurants) {
        this.restaurants = restaurants;
        sortOrderByRestaurant(orders);
        getFilteredOrders(sortedOrders);
    }

    /**
     * Get orders from a specific date and filter out the invalid orders.
     * @return List of valid orders for a specific date.
     */
    public void getFilteredOrders(ArrayList<Order> orders) {
        for (Order ord : orders) {
            if (!validCreditCard(ord)) {
                validatedOrders.add(ord);
                continue;
            }
            if (ord.getOrderItems().length < 1 || ord.getOrderItems().length > 4) {
                ord.setOrderOutcome(OrderOutcome.InvalidPizzaCount);
                validatedOrders.add(ord);
                continue;
            }
            if (ord.getCvv().length() != 3) {
                ord.setOrderOutcome(OrderOutcome.InvalidCvv);
                validatedOrders.add(ord);
                continue;
            }
            if (!ord.arePizzasFromSameRestaurant(restaurants, ord.getOrderItems())) {
                ord.setOrderOutcome(OrderOutcome.InvalidPizzaCombinationMultipleSuppliers);
                validatedOrders.add(ord);
                continue;
            }
            if (ord.getDeliveryCost(ord.restaurantOrdered(restaurants)) !=
                    ord.getPriceTotalInPence()) {
                ord.setOrderOutcome(OrderOutcome.InvalidTotal);
                validatedOrders.add(ord);
                continue;
            }

            // Otherwise, the order is valid.
            ord.setOrderOutcome(OrderOutcome.ValidButNotDelivered);
            validatedOrders.add(ord);
        }
    }

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9]+");
    }

    /**
     * Checks whether the credit card used in an order is valid.
     * @param order The order being checked.
     * @return True if the credit card is valid, false otherwise.
     */
    public boolean validCreditCard(Order order) {
        // Check cardExpiry
        SimpleDateFormat formatterExpiry = new SimpleDateFormat("MM/yy");
        SimpleDateFormat formatterOrderDate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateExpiry = formatterExpiry.parse(order.getCreditCardExpiry());
            Date dateOrder = formatterOrderDate.parse(order.getOrderDate());

            if (dateExpiry.before(dateOrder)) {
                order.setOrderOutcome(OrderOutcome.InvalidExpiryDate);
                return false;
            }
        } catch (ParseException e) {
            order.setOrderOutcome(OrderOutcome.InvalidExpiryDate);
            return false;}

        // Check cvv
        if (order.getCvv().length() != 3 || !isNumeric(order.getCvv())) {
            order.setOrderOutcome(OrderOutcome.InvalidCvv);
            return false;
        }

        // Check credit card number
        if (!isNumeric(order.getCreditCardNumber()) || order.getCreditCardNumber().length() != 16) {
            // Check if it is either Visa or Mastercard from IINs
            order.setOrderOutcome(OrderOutcome.InvalidCardNumber);
            return false;
        }

        if (!validIIN(order)) {
            order.setOrderOutcome(OrderOutcome.InvalidCardNumber);
            return false;
        }

        if (!luhnAlgorithm(order.getCreditCardNumber())) {
            order.setOrderOutcome(OrderOutcome.InvalidCardNumber);
            return false;
        }

        return true;

    }

    /**
     * Checks if the credit card number is valid using the Luhn algorithm.
     * @param cardNo The credit card number to be checked.
     * @return True if the credit card number is valid, false otherwise.
     */
    public boolean luhnAlgorithm(String cardNo) {
        int cardLength = cardNo.length();

        int totalSum = 0;
        boolean skip = true;
        for (int i = cardLength - 1; i >= 0; i--)
        {

            // Converts char to its integer value.
            int digit = cardNo.charAt(i) - '0';

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
     * @param order Order to check.
     * @return True if the credit card IIN is valid.
     */
    public boolean validIIN(Order order) {
        try {
            long firstDigit = Long.parseLong(order.getCreditCardNumber().substring(0, 1));
            long fourDigits = Long.parseLong(order.getCreditCardNumber().substring(0, 4));
            long twoDigits = Long.parseLong(order.getCreditCardNumber().substring(0, 2));

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
            order.setOrderOutcome(OrderOutcome.InvalidCardNumber);
            return false;
        }

        return false;
    }

    /**
     * Sorts the ArrayList of orders by their restaurants, where the restaurant closer to the start goes
     * first.
     * @return An ArrayList of Orders, where orders from the closest restaurant are first.
     */
    public void sortOrderByRestaurant(ArrayList<Order> orders) {
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
                sortedOrders.addAll(map.get(rest));
            }
        }

    }

    public ArrayList<Order> getValidatedOrders() {
        return validatedOrders;
    }
}
