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
        LngLat appleton = new LngLat(-3.1869,55.9445);
        boolean test = appleton.inCentralArea();
        assertTrue(test);
    }

    public void testNotInCentralArea() {
        LngLat point = new LngLat(-5,60);
        boolean test = point.inCentralArea();
        assertFalse(test);

        LngLat izq = new LngLat(-3.1942462451488,55.9446287639296);
        boolean izqResult = izq.inCentralArea();
        assertFalse(izqResult);

        LngLat sameLevelOfEdge = new LngLat(-3.1975280615679, 55.9462329999972);
        boolean result = (sameLevelOfEdge.inCentralArea());
        assertFalse(result);
    }

    public void testCornersInCentralArea() {
        LngLat corner1 = new LngLat(-3.192473, 55.946233);
        boolean testCorner1 = corner1.inCentralArea();
        assertTrue(testCorner1);

        LngLat corner2 = new LngLat(-3.192473, 55.942617);
        boolean testCorner2 = corner2.inCentralArea();
        assertTrue(testCorner2);

        LngLat corner3 = new LngLat(-3.184319, 55.942617);
        boolean testCorner3 = corner3.inCentralArea();
        assertTrue(testCorner3);

        LngLat corner4 = new LngLat(-3.184319, 55.946233);
        boolean testCorner4 = corner4.inCentralArea();
        assertTrue(testCorner4);
    }

    public void testInLineLeft() {
        LngLat point = new LngLat(-3.192473, 55.944425);
        boolean test = point.inCentralArea();
        assertTrue(test);
    }

    public void testInLineRight() {
        LngLat point = new LngLat(-3.184319, 55.944425);
        boolean test = point.inCentralArea();
        assertTrue(test);
    }

    public void testInLineUp() {
        LngLat point = new LngLat(-3.188396, 55.946233);
        boolean test = point.inCentralArea();
        assertTrue(test);
    }

    public void testInLineDown() {
        LngLat point = new LngLat(-3.188396, 55.942617);
        boolean test = point.inCentralArea();
        assertTrue(test);
    }

    public void testDistanceTo() {
        LngLat point = new LngLat(-3.1869,55.9445);
        LngLat point2 = new LngLat(-5,60);
        double distance = point.distanceTo(point2);
        assertEquals(4.44234, Math.round(distance* 100000d) / 100000d);

        // Distance to itself
        double distance2= point.distanceTo(point);
        assertEquals(0.0,distance2);
    }

    public void testCloseTo() {
        LngLat point = new LngLat(-3.1869,55.9445);
        LngLat point2 = new LngLat(-3.1869 + 0.00014,55.9445);
        boolean isClose = point.closeTo(point2);
        assertTrue(isClose);
    }

    public void testNextPositionEast() {
        LngLat point = new LngLat(0.0,0.0);
        LngLat nextPos = point.nextPosition(Compass.EAST);
        LngLat expected = new LngLat(0.00015,0.0);
        assertEquals(expected.lng(),nextPos.lng());
        assertEquals(expected.lat(),nextPos.lat());
    }

    public void testNextPositionNorth() {
        LngLat point = new LngLat(0.0,0.0);
        LngLat nextPos = point.nextPosition(Compass.NORTH);
        LngLat expected = new LngLat(0.0,0.00015);
        assertEquals(expected.lng(),nextPos.lng(), 1e-12);
        assertEquals(expected.lat(),nextPos.lat(), 1e-12);
    }

    public void testNextPositionWest() {
        LngLat point = new LngLat(0.0,0.0);
        LngLat nextPos = point.nextPosition(Compass.WEST);
        LngLat expected = new LngLat(-0.00015,0.0);
        assertEquals(expected.lng(),nextPos.lng(), 1e-12);
        assertEquals(expected.lat(),nextPos.lat(), 1e-12);
    }

    public void testNextPositionSouth() {
        LngLat point = new LngLat(0.0,0.0);
        LngLat nextPos = point.nextPosition(Compass.SOUTH);
        LngLat expected = new LngLat(0.0,-0.00015);
        assertEquals(expected.lng(),nextPos.lng(), 1e-12);
        assertEquals(expected.lat(),nextPos.lat(), 1e-12);
    }

    public void testNextPositionHovering() {
        LngLat point = new LngLat(1.0,2.0);
        LngLat nextPos = point.nextPosition(null);
        assertEquals(point.lng(),nextPos.lng());
        assertEquals(point.lat(),nextPos.lat());

    }
}
