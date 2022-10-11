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

        // Tests for random points inside the polygon
        LngLat point = new LngLat(-3.1869,55.9445);
        boolean test = point.inCentralArea();
        assertTrue(test);


    }

    public void testNotInCentralArea() {

        LngLat point2 = new LngLat(-5,60);
        boolean test2 = point2.inCentralArea();
        assertFalse(test2);
    }

    public void testCornersInCentralArea() {
        LngLat corner1 = new LngLat(-3.190578818321228, 55.94402412577528);
        boolean testCorner1 = corner1.inCentralArea();
        assertTrue(testCorner1);

        LngLat corner2 = new LngLat(-3.1899887323379517, 55.94284650540911);
        boolean testCorner2 = corner2.inCentralArea();
        assertTrue(testCorner2);

        LngLat corner3 = new LngLat(-3.187097311019897, 55.94328811724263);
        boolean testCorner3 = corner3.inCentralArea();
        assertTrue(testCorner3);

        LngLat corner4 = new LngLat(-3.190578818321228, 55.94402412577528);
        boolean testCorner4 = corner4.inCentralArea();
        assertTrue(testCorner4);
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
        assertEquals(expected.lng(),nextPos.lng());
        assertEquals(expected.lat(),nextPos.lat());
    }
}
