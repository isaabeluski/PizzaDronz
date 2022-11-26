package uk.ac.ed.inf;

import java.net.URL;
import java.util.ArrayList;

public class dayOrder {

    String date;
    Order[] orders;
    Restaurant[] restaurants;

    dayOrder(String date, Order[] orders, Restaurant[] restaurants) {
        this.date = date;
        this.orders = orders;
        this.restaurants = restaurants;
    }

    /**
     * Get orders from a specific date and filter out the invalid orders.
     * @return List of valid orders for a specific date.
     */
    public ArrayList<Order> getFilteredOrders() {
        ArrayList<Order> filteredOrders = new ArrayList<>();
        for (Order loool : orders) {
            //LocalDate cardExpiry = stringToDate(order.creditCardExpiry);
            //LocalDate orderDate = stringToDate(order.orderDate);
            if (loool.getOrderItems().length < 1 || loool.getOrderItems().length > 4) {
                loool.setOrderOutcome(OrderOutcome.InvalidPizzaCount);
                continue;
            }
            /*
            if (cardExpiry.isBefore(orderDate)) {
                order.orderOutcome = OrderOutcome.InvalidExpiryDate;
                continue;
            }
             */
            if (loool.getCvv().length() != 3) {
                loool.setOrderOutcome(OrderOutcome.InvalidCvv);
                continue;
            }
            if (!loool.arePizzasFromSameRestaurant(restaurants, loool.getOrderItems())) {
                loool.setOrderOutcome(OrderOutcome.InvalidPizzaCombinationMultipleSuppliers);
                continue;
            }
            if (loool.getDeliveryCost(loool.restaurantOrdered(restaurants)) !=
                    loool.getPriceTotalInPence()) {
                loool.setOrderOutcome(OrderOutcome.InvalidTotal);
            }
            loool.setOrderOutcome(OrderOutcome.ValidButNotDelivered);
            filteredOrders.add(loool);
        }
        return filteredOrders;
    }

}
