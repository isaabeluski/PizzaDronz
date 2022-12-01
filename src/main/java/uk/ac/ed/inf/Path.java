package uk.ac.ed.inf;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Represents the path the drone will take from the start to its destination.
 */
public class Path {

    private static final NoFlyZones noFlyZones = NoFlyZones.getInstance();
    private static ArrayList<Flightpath> flightpath = new ArrayList<>();

    /**
     * Function that calculates the path the drone will need to make, using A* Algorithm.
     * @param destination Where the drone needs to end up 'close' to.
     * @return A list with all the points (representing the steps) the drone needs to take.
     */
    private static ArrayList<LngLat> getPathPoints(Node start, Node destination, Order order) {

        PriorityQueue<Node> openList = new PriorityQueue<>();
        ArrayList<Node> closedList = new ArrayList<>();


        start.calculateHeuristic(destination);
        start.setG(0.0);
        start.calculateF();

        openList.add(start);

        while(!openList.isEmpty()) {

            // Get node with lowest F
            Node currentNode = openList.peek();
            LngLat currentPoint = currentNode.getPoint();

            // Stop if we find the destination node
            if (currentPoint.closeTo(destination.getPoint())) {
                // Backtrack to get path
                return backtrack(currentNode, order);
            }

            // Finds all neighbours of current node.
            ArrayList<Node> neighbours = currentNode.findLegalNeighbours();

            for (Node neighbour : neighbours) {

                // If the neighbour can be used
                if (!noFlyZones.intersecting(new Line2D.Double(currentPoint.lng(), currentPoint.lat(), neighbour.getPoint().lng(), neighbour.getPoint().lat()))) {
                    LngLat neighbourPoint = neighbour.getPoint();


                    // Calculates G
                    double distanceTravelled = currentNode.getG() + currentPoint.distanceTo(neighbourPoint);

                    // If either closedList or openList contain the neighbour, check if this path is shorter.
                    if (closedList.contains(neighbour) || openList.contains(neighbour)) {
                        if (distanceTravelled < neighbour.getG()) {
                            neighbour.setG(distanceTravelled);
                            neighbour.calculateHeuristic(destination);
                            neighbour.calculateF();
                            neighbour.setParent(currentNode);

                            if (closedList.contains(neighbour)) {
                                closedList.remove(neighbour);
                                openList.add(neighbour);
                            }
                        }
                        continue;
                    }

                    // If it is a new node
                    if (!closedList.contains(neighbour) && !openList.contains(neighbour)) {
                        neighbour.setG(distanceTravelled);
                        neighbour.calculateHeuristic(destination);
                        neighbour.calculateF();
                        neighbour.setParent(currentNode);
                        openList.add(neighbour);
                    }
                }
            }

            closedList.add(currentNode);
            openList.remove(currentNode);
        }
        // No path could be found :(
        return null;
    }

    private static ArrayList<LngLat> backtrack(Node currentNode, Order order) {
        ArrayList<LngLat> path = new ArrayList<>();
        path.add(currentNode.getPoint());

        while (currentNode.getParent() != null) {
            LngLat parentPoint = currentNode.getParent().getPoint();
            path.add(parentPoint);

            if (currentNode.getParent() != null) {
                flightpath.add(new Flightpath(
                        order.getOrderNo(),
                        parentPoint.lng(),
                        parentPoint.lat(),
                        currentNode.getDirection().getAngle(),
                        currentNode.getPoint().lng(),
                        currentNode.getPoint().lat()));
            }

            currentNode = currentNode.getParent();
        }
        Collections.reverse(path);
        Collections.reverse(flightpath);
        return path;
    }

    public static ArrayList<LngLat> pathToStart(ArrayList<LngLat> path) {
        Collections.reverse(path);
        Collections.reverse(flightpath);
        return path;
    }

    public static ArrayList<LngLat> totalPath(Node start, Node destination, Order order) {
        ArrayList<LngLat> totalPath = getPathPoints(start, destination, order);
        assert totalPath != null;
        totalPath.addAll(pathToStart(totalPath));
        return totalPath;
    }

    public static ArrayList<Flightpath> getFlightpath() {
        return flightpath;
    }
}


