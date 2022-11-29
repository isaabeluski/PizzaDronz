package uk.ac.ed.inf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Represents the path the drone will take from the start to its destination.
 */
public class Path {

    /**
     * Function that calculates the path the drone will need to make, using A* Algorithm.
     * @param destination Where the drone needs to end up 'close' to.
     * @return A list with all the points (representing the steps)
     */
    public static ArrayList<LngLat> getPathPoints(Node start, Node destination, ArrayList<ArrayList<LngLat>> noFlyZones) {
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
                ArrayList<LngLat> path = new ArrayList<>();
                path.add(currentPoint);
                while (currentNode.getParent() != null) {
                    LngLat getPoint = currentNode.getParent().getPoint();
                    path.add(getPoint);
                    currentNode = currentNode.getParent();
                }
                Collections.reverse(path);
                return path;
            }

            // Finds all legal neighbours of current node.
            ArrayList<Node> neighbours = currentNode.findLegalNeighbours();

            for (Node neighbour : neighbours) {

                // If the neighbour can be used
                if (!currentNode.intersects(neighbour, noFlyZones)) {

                    LngLat neighbourPoint = neighbour.getPoint();

                    // In order to control if we have found the same node
                    if (all.containsKey(neighbourPoint)) {
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

    public static ArrayList<LngLat> pathToStart(ArrayList<LngLat> path) {
        Collections.reverse(path);
        return path;
    }

}
