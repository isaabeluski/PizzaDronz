package uk.ac.ed.inf;

/**
 * Represents an order.
 */
public class Order {

    private String orderNo;
    private String orderDate;
    private String customer;
    private String creditCardNumber;
    private String creditCardExpiry;
    private String ccv;
    private int priceTotalInPence;
    private String[] orderItems;

    /**
     * Calculates the cost in pence of having all the pizzas delivered by the drone, including the standard
     * delivery charge of 1 pound per delivery.
     * @param restaurants The list of participating restaurants.
     * @param pizzasOrdered Variable number of strings for the individual pizzas ordered.
     * @return The cost of the pizzas being delivered by the drone in pence.
     * @throws InvalidPizzaCombinationException if the order consists of an invalid combination.
     */
    public int getDeliveryCost(Restaurant[] restaurants, String... pizzasOrdered) throws InvalidPizzaCombinationException {
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

        // If the pizza doesn't correspond to any restaurant
        if (restaurantOrdered == null) {
            throw new InvalidPizzaCombinationException("pizza doesnt exist");
        }

        int count=0;
        // Check if all pizzas come from the same restaurant
        for (String pizza : pizzasOrdered) {
            for (Menu dish : restaurantOrdered.getMenu()) {
                if (pizza.equals(dish.getName())) {
                    count++;
                    break;
                }
            }
        }

        if (count != pizzasOrdered.length) {
            throw new InvalidPizzaCombinationException("pizza's don't come from the same restaurant");
        }

        // Calculates cost
        Menu[] menus = restaurantOrdered.getMenu();
        for (String s : pizzasOrdered) {
            for (Menu menu : menus) {
                if (menu.getName().equals(s)) {
                    cost += menu.getPriceInPence();
                    break;
                }
            }
        }

        if (cost != 0) {
            cost +=100;
        }

        return cost;
    }

}
