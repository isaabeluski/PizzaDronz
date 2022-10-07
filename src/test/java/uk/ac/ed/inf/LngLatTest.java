package uk.ac.ed.inf;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.net.MalformedURLException;
import java.util.List;

public class LngLatTest
    extends  TestCase
{
    public void testInCentralArea() {
        List<LngLat> areas = CentralAreaClient.getInstance().centralCoordinates();
        assertEquals(5, areas.size());

        LngLat point = new LngLat(-3.1869,55.9445);
        boolean test = point.inCentralArea();
        assertTrue(test);

        LngLat point2 = new LngLat(-5,60);
        boolean test2 = point2.inCentralArea();
        assertFalse(test2);

    }
}
