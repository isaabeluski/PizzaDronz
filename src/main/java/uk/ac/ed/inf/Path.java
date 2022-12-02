package uk.ac.ed.inf;

import java.util.*;

/**
 * Represents the path the drone will take from the start to its destination.
 */
public class Path {
    private static final NoFlyZones noFlyZones = NoFlyZones.getInstance();
    private static final ArrayList<Flightpath> flightpath = new ArrayList<>();

    private HashMap<Restaurant, ArrayList<LngLat>> allPaths = new HashMap<>();

    public Path(Restaurant[] restaurants) {
        this.allPaths = allPaths(restaurants);
    }

    /**
     * Function that calculates the path the drone will need to make, using A* Algorithm.
     * @param destination Where the drone needs to end up 'close' to.
     * @return A list with all the points (representing the steps) the drone needs to take.
     */
    public static ArrayList<LngLat> getPathPoints(Node start, Node destination) {

        PriorityQueue<Node> openList = new PriorityQueue<>();
        ArrayList<Node> closedList = new ArrayList<>();
        HashMap<LngLat, Node> all = new HashMap<>();

        start.calculateHeuristic(destination);
        start.setG(0.0);
        start.calculateF();
        all.put(start.getPoint(), start);

        openList.add(start);

        while(!openList.isEmpty()) {

            // Get node with lowest F
            Node currentNode = openList.peek();
            LngLat currentPoint = currentNode.getPoint();

            // Stop if we find the destination node
            if (currentPoint.closeTo(destination.getPoint())) {
                // Backtrack to get path
                return backtrack(currentNode);
            }

            // Finds all neighbours of current node.
            ArrayList<Node> neighbours = currentNode.findNeighbours();

            for (Node neighbour : neighbours) {

                // If the neighbour can be used
                if (!noFlyZones.isIntersecting(currentNode,neighbour))  {
                    LngLat neighbourPoint = neighbour.getPoint();

                    if (all.containsKey(neighbourPoint)) { //as DS is not a graph, need to check if node is new or not.
                        neighbour = all.get(neighbourPoint);
                    } else {
                        all.put(neighbourPoint, neighbour);
                    }

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

    private static ArrayList<LngLat> backtrack(Node currentNode) {
        ArrayList<LngLat> path = new ArrayList<>();
        path.add(currentNode.getPoint());

        while (currentNode.getParent() != null) {
            LngLat parentPoint = currentNode.getParent().getPoint();
            path.add(parentPoint);

            /*
            if (currentNode.getParent() != null) {
                flightpath.add(new Flightpath(
                        order.getOrderNo(),
                        parentPoint.lng(),
                        parentPoint.lat(),
                        currentNode.getDirection().getAngle(),
                        currentNode.getPoint().lng(),
                        currentNode.getPoint().lat()));
            }
             */

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

    public static ArrayList<LngLat> totalPath(Node start, Node destination) {
        ArrayList<LngLat> path = getPathPoints(start, destination);
        ArrayList<LngLat> totalPath = new ArrayList<>(path);
        Collections.reverse(path);
        totalPath.addAll(path);
        return totalPath;
    }

    public HashMap<Restaurant, ArrayList<LngLat>> allPaths(Restaurant[] restaurants) {
        HashMap<Restaurant, ArrayList<LngLat>> paths = new HashMap<>();
        for (Restaurant restaurant : restaurants) {
            LngLat restaurantLngLat = new LngLat(restaurant.getLng(), restaurant.getLat());
            paths.put(restaurant, Path.totalPath(Drone.APPLETON_TOWER.toNode(), restaurantLngLat.toNode()));
        }
        return paths;
    }

    public ArrayList<Flightpath> getMoves(Order order) {
        ArrayList<Flightpath> moves = new ArrayList<>();
        this.path.forEach(node -> {
            if (node.getParent() != null) {
                moves.add(new Flightpath(
                        order.getOrderNo(),
                        node.getParent().getPoint().lng(),
                        node.getParent().getPoint().lat(),
                        node.getDirection().getAngle(),
                        node.getPoint().lng(),
                        node.getPoint().lat()));
            }
        });
        return moves;
    }


    public static ArrayList<Flightpath> getFlightpath() {
        return flightpath;
    }

    public HashMap<Restaurant, ArrayList<LngLat>> getAllPaths() {
        return allPaths;
    }
}


