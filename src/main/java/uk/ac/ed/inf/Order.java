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
    public int getDeliveryCost(Restaurant[] restaurants, String... pizzasOrdered) {

        try {

            if (restaurants == null || pizzasOrdered == null) {
                throw new NullPointerException("Parameters cannot be null");
            }

            if (pizzasOrdered.length < 1 || pizzasOrdered.length > 4) {
                throw new InvalidPizzaCombinationException("There has to be a minimum of 1 and maximum of 4 pizzas.");
            }

            int cost = 100;
            Restaurant restaurantOrdered = null;

            // Finds the restaurant where the order is being made to.
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

            // If the pizza does not correspond to any restaurant
            if (restaurantOrdered == null) {
                throw new InvalidPizzaCombinationException("Pizza does not exist!");
            }

            // Checks if all pizzas come from the same restaurant.
            int count = 0;
            for (String pizza : pizzasOrdered) {
                for (Menu dish : restaurantOrdered.getMenu()) {
                    if (pizza.equals(dish.getName())) {
                        count++;
                        break;
                    }
                }
            }

            if (count != pizzasOrdered.length) {
                throw new InvalidPizzaCombinationException("Pizzas do not come from the same restaurant");
            }

            // Calculates cost
            Menu[] menus = restaurantOrdered.getMenu();
            for (String pizza : pizzasOrdered) {
                for (Menu menu : menus) {
                    if (menu.getName().equals(pizza)) {
                        cost += menu.getPriceInPence();
                        break;
                    }
                }
            }

            return cost;

        } catch(InvalidPizzaCombinationException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
