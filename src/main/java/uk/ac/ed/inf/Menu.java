package uk.ac.ed.inf;

/**
 * Represents the menu of a restaurant.
 */
public class Menu {
    private String name;
    private int priceInPence;

    /**
     * Default constructor.
     */
    Menu() {
    }

    /**
     * Gets the name of a pizza.
     * @return The name of the pizza.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the price in pence of the pizza.
     * @return The price of the pizza.
     */
    public int getPriceInPence() {
        return this.priceInPence;
    }
}
