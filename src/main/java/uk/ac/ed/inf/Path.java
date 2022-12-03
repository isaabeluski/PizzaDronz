package uk.ac.ed.inf;

import java.awt.geom.Line2D;
import java.time.Clock;
import java.time.Duration;
import java.util.*;

/**
 * Represents the path the drone will take from the start to its destination.
 */
public class Path {
    private static final NoFlyZones noFlyZones = NoFlyZones.getInstance();
    private static final CentralAreaClient centralArea = CentralAreaClient.getInstance();
    private ArrayList<Node> goToRestaurant = new ArrayList<>();
    private ArrayList<Node> goToStart = new ArrayList<>();

    public Path(LngLat start, LngLat destination) {
        this.goToRestaurant = getPathPoints(start.toNode(), destination.toNode());
        this.goToStart = getPathPoints(destination.toNode(), start.toNode());
    }

    /**
     * Function that calculates the path the drone will need to make, using A* Algorithm.
     * @param destination Where the drone needs to end up 'close' to.
     * @return A list with all the points (representing the steps) the drone needs to take.
     */
    public ArrayList<Node> getPathPoints(Node start, Node destination) {
        Clock baseClock = Clock.systemUTC();
        Duration duration = Duration.ofNanos(1);
        System.out.println(baseClock.instant().getNano());

        PriorityQueue<Node> openList = new PriorityQueue<>();
        ArrayList<Node> closedList = new ArrayList<>();

        start.calculateHeuristic(destination);
        start.setG(0.0);
        start.calculateF();
        boolean isInCentral = start.getPoint().inCentralArea();
        int count = 0;

        openList.add(start);

        while(!openList.isEmpty()) {

            // Get node with lowest F
            Node currentNode = openList.peek();
            LngLat currentPoint = currentNode.getPoint();
            int intersectionsCentralArea = 0;

            // Stop if we find the destination node
            if (currentPoint.closeTo(destination.getPoint())) {
                ArrayList<Node> path = new ArrayList<>();
                // Backtrack to get path
                while (currentNode.getParent() != null) {
                    LngLat parentPoint = currentNode.getParent().getPoint();
                    path.add(currentNode.getParent());
                    Clock ticks = Clock.tick(baseClock, duration);
                    System.out.println(ticks.instant().getNano());
                    currentNode = currentNode.getParent();
                };
                Collections.reverse(path);
                return path;
            }

            // Finds all neighbours of current node.
            ArrayList<Node> neighbours = currentNode.findNeighbours();

            for (Node neighbour : neighbours) {

                // Makes sure it can only get out of central area once, or enter central area once.
                if (neighbour.getPoint().inCentralArea() != isInCentral) {
                    isInCentral = neighbour.getPoint().inCentralArea();
                    count++;
                    if (count > 1) {
                        continue;
                    }
                }

                // If the neighbour can be used
                if (!noFlyZones.intersectsNoFlyZone(currentNode,neighbour)) {
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
        return null;
    }

    /**
     * Reverses the path the drone will take.
     * @param currentNode The node the drone is currently at.
     * @return The path the drone should follow.
     */
    private ArrayList<Node> backtrack(Node currentNode) {
        ArrayList<Node> path = new ArrayList<>();
        path.add(currentNode);

        while (currentNode.getParent() != null) {
            LngLat parentPoint = currentNode.getParent().getPoint();
            path.add(currentNode.getParent());

            currentNode = currentNode.getParent();
        }
        Collections.reverse(path);
        this.goToRestaurant = path;
        return path;
    }

    /**
     * Stores each path the drone should take for every restaurant.
     * @param start The starting point of the drone.
     * @param restaurants The list of restaurants the drone needs to visit.
     * @return Hashmap with the path the drone should take for each restaurant.
     */
    public static HashMap<Restaurant, Path> allPaths(LngLat start, Restaurant[] restaurants) {
        HashMap<Restaurant, Path> paths = new HashMap<>();
        for (Restaurant restaurant : restaurants) {
            LngLat restaurantLngLat = new LngLat(restaurant.getLng(), restaurant.getLat());
            paths.put(restaurant, new Path(start, restaurantLngLat));
        }
        return paths;
    }

    /**
     * Creates a Flighpath list representing the path the drone should take.
     * @param order The order the drone is currently delivering.
     * @param path The path that that needs to be followed to get to the destination.
     * @return List of FlightPaths representing the path the drone should take.
     */
    public static ArrayList<Flightpath> getMoves(Order order, ArrayList<Node> path) {
        ArrayList<Flightpath> moves = new ArrayList<>();
        for (var node : path){
            if (node.getParent() != null) {
                moves.add(new Flightpath(
                        order.getOrderNo(),
                        node.getParent().getPoint(),
                        node.getDirection().getAngle(),
                        node.getPoint()));
            }
        }
        return moves;
    }


    /**
     * Converts a list of nodes into a list of LngLat points.
     * @param path The path the drone needs to follow.
     * @return List of LngLat points representing the path the drone needs to follow.
     */
    public static ArrayList<LngLat> toLngLatList(ArrayList<Node> path) {
        ArrayList<LngLat> lngLatList = new ArrayList<>();
        for (Node node : path) {
            lngLatList.add(node.getPoint());
        }
        return lngLatList;
    }


    // GETTERS
    public ArrayList<Node> getGoToRestaurant() {
        return this.goToRestaurant;
    }

    public ArrayList<Node> getGoToStart() {
        return this.goToStart;
    }
}


