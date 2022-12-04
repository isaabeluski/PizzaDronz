package uk.ac.ed.inf;

import java.util.*;

/**
 * Represents the path the drone will take from the start to its destination.
 */
public class Path {
    private static final NoFlyZones noFlyZones = NoFlyZones.getInstance();
    private static final CentralArea centralArea = CentralArea.getInstance();
    private final ArrayList<Node> pathInNodes = new ArrayList<>();
    private final ArrayList<LngLat> pathInLngLat = new ArrayList<>();

    public Path(LngLat start, LngLat destination) {
        calculatePath(start, destination);
    }

    /**
     * Calculates the shortest path the drone can take using the A* Algorithm.
     * @param destination Where the drone needs to end up 'close' to.
     * @return A list with all the points of the path the drone should go through to get to the destination.
     */
    public static Node performAStar(Node start, Node destination) {

        PriorityQueue<Node> openList = new PriorityQueue<>();
        ArrayList<Node> closedList = new ArrayList<>();

        start.calculateHeuristic(destination);
        start.setG(0.0);
        start.calculateF();
        int intersectionsCentralArea = 0;

        openList.add(start);

        while (!openList.isEmpty()) {

            // Gets node with lowest F score.
            Node currentNode = openList.peek();
            LngLat currentPoint = currentNode.getPoint();

            // Sets the time it took to calculate the move.
            currentNode.setTicks(System.nanoTime() - Drone.startTime);

            // Stop if current node is 'close to' the destination point.
            if (currentPoint.closeTo(destination.getPoint())) {
                return currentNode;
            }

            // Finds all neighbours of current node.
            ArrayList<Node> neighbours = currentNode.findNeighbours();

            for (Node neighbour : neighbours) {

                // Makes sure it can only get out of central area once, or enter central area once.
                if (centralArea.intersectsCentralArea(currentNode, neighbour)) {
                    intersectionsCentralArea++;
                    if (intersectionsCentralArea > 1) {
                        continue;
                    }
                }

                // If the neighbour can be used
                if (!noFlyZones.intersectsNoFlyZone(currentNode, neighbour)) {
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
        // No path could be found - add exception.
        return null;
    }

    /**
     * Calculates the path the drone will take.
     * @param start Where the drone starts.
     * @param destination Where the drone needs to end up 'close' to.
     */
    private void calculatePath(LngLat start, LngLat destination) {

        // Get nodes
        Node startNode = start.toNode();
        Node destinationNode = destination.toNode();

        // Perform A*
        Node node = performAStar(startNode, destinationNode);

        // Backtrack to get path
        ArrayList<Node> path = new ArrayList<>();
        path.add(node);

        while (node.getParent() != null) {
            LngLat parentPoint = node.getParent().getPoint();
            path.add(node.getParent());

            node = node.getParent();
        }
        Collections.reverse(path);

        pathInNodes.addAll(path);
        pathInLngLat.addAll(pathToLngLat(path));

        // Add hover movement
        node.setParent(node);
        Node hover = new Node(node.getPoint());
        path.add(hover);

    }

    /**
     * Creates a Flightpath list representing the moves the drone takes in a trip.
     * @param order The order the drone is currently delivering.
     * @return List of FlightPaths representing the moves the drone should take.
     */
    public ArrayList<Flightpath> getMoves(Order order) {
        ArrayList<Flightpath> moves = new ArrayList<>();
        for (var node : pathInNodes) {
            if (node.getParent() != null) {
                moves.add(new Flightpath(
                        order.getOrderNo(),
                        node.getParent().getPoint(),
                        node.getAngle(),
                        node.getPoint(),
                        node.getTicks()));
            }
        }
        return moves;
    }


    /**
     * Converts a list of nodes into a list of LngLat points.
     * @return List of LngLat points representing the path the drone needs to follow.
     */
    private ArrayList<LngLat> pathToLngLat(ArrayList<Node> path) {
        ArrayList<LngLat> lngLatList = new ArrayList<>();
        for (Node node : path) {
            lngLatList.add(node.getPoint());
        }
        return lngLatList;
    }

    public ArrayList<LngLat> getPathInLngLat() {
        return pathInLngLat;
    }

}


