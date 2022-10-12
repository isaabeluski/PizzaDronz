package uk.ac.ed.inf;

import junit.framework.TestCase;

import java.net.MalformedURLException;
import java.net.URL;

public class OrderTest extends TestCase {

    String baseUrl = "https://ilp-rest.azurewebsites.net/restaurants";

    public void testGetDeliveryCost() throws MalformedURLException, InvalidPizzaCombinationException {
        Restaurant[] restaurants = Restaurant.getRestaurantFromRestServer(new URL(baseUrl));
        Order order = new Order();
        int cost = order.getDeliveryCost(restaurants, "Meat Lover");
        assertEquals(1500, cost);

    }
}
