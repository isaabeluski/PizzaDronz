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

    public void testDistanceTo() {
        LngLat point = new LngLat(-3.1869,55.9445);
        LngLat point2 = new LngLat(-5,60);
        double distance = point.distanceTo(point2);
        assertEquals(4.44234, Math.round(distance* 100000d) / 100000d);
        double distance2= point.distanceTo(point);
        assertEquals(0.0,distance2);
    }

    public void testCloseTo() {
        LngLat point = new LngLat(-3.1869,55.9445);
        LngLat point2 = new LngLat(-3.1869 + 0.00014,55.9445);
        boolean isClose = point.closeTo(point2);
        assertTrue(isClose);
    }

    public void testNextPosition() {
        LngLat point = new LngLat(0.0,0.0);
        LngLat nextPos = point.nextPosition(Compass.EAST);
        LngLat expected = new LngLat(0.0,0.00015);
        assertEquals(expected.getLng(),nextPos.getLng());
        assertEquals(expected.getLat(),nextPos.getLat());
    }
}
