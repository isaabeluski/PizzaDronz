package uk.ac.ed.inf;

import junit.framework.TestCase;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Unit Test for Order class.
 */

public class OrderTest extends TestCase {

    /*
    String baseUrl = "https://ilp-rest.azurewebsites.net/";
    Restaurant[] restaurants = Restaurant.getRestaurantFromRestServer(new URL(baseUrl));
    Order order = new Order();

    public OrderTest() throws MalformedURLException {
    }

    public void testGetDeliveryCost() {
        int cost = order.getDeliveryCost(restaurants, "Meat Lover");
        assertEquals(1500, cost);

    }

    public void testPizzaDoesNotExist() {
        int cost = order.getDeliveryCost(restaurants, "randomPizza");
        assertEquals(0, cost);
    }

    public void testMultipleDifferentPizzas(){
        int cost = order.getDeliveryCost(restaurants, "Meat Lover", "Vegan Delight");
        assertEquals(2600, cost);
    }

    public void testDuplicatedPizzas() {
        int cost = order.getDeliveryCost(restaurants, "Proper Pizza", "Proper Pizza", "Proper Pizza");
        assertEquals(4300, cost);
    }

    public void testZeroPizzas() {
        int cost = order.getDeliveryCost(restaurants);
        assertEquals(0, cost);
    }

    public void testTooManyPizzas(){
        int cost = order.getDeliveryCost(restaurants, "Meat Lover", "Vegan Delight", "somePizza", "otherPizza");
        assertEquals(0, cost);
    }

    public void testPizzasFromDifferentRestaurants() {
        int cost = order.getDeliveryCost(restaurants, "Meat Lover", "Proper Pizza");
        assertEquals(0, cost);
    }

     */
}
