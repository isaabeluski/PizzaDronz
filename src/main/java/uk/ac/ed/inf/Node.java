package uk.ac.ed.inf;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Node implements Comparable<Node>{

    private LngLat point;
    private Double F;
    private Double G = 0.0;
    private Double H = 0.0;
    private Node parent;
    private ArrayList<ArrayList<LngLat>> noFlyZones = NoFlyZones.getNoFlyZonesFromServer();
    private Compass direction;

    public Node(LngLat point)  {
        this.point = point;
    }

    public void calculateF() {
        this.F = this.G + this.H;
    }

    /**
     * Compares two nodes with respect to their scores F.
     * @param node the object to be compared.
     * @return 0 if values are equal, 1 if node.F is higher and -1 if lower.
     */
    @Override
    public int compareTo(Node node) {
        return Double.compare(this.F, node.F);
    }

    /**
     * Calculates the heuristic for the A* algorithm, where the Euclidean distance is used.
     * @param destination A node representing the final destination of a trip.
     */
    public void calculateHeuristic(Node destination) {
        this.H = this.point.distanceTo(destination.point);
    }

    /**
     * Gets all possible positions the drone can make from a node.
     * @param destination The final destination.
     * @return A list of nodes representing the possible next position.
     */
    public ArrayList<Node> findLegalNeighbours(Node destination) {
        ArrayList<Node> legalNeighbours = new ArrayList<>();
        Compass[] values = Compass.class.getEnumConstants();
        for (Compass compassDirection : values) {

            // If you can go in a straight line, you don't need to check the neighbours behind the currentPoint
            if (!this.intersects(noFlyZones, destination)) {
                double angle = this.getAngleFromLine(destination);
                double angleNeighbour = compassDirection.getAngle();
                double range1 = getRangeAngle(angle+20);
                double range2 = getRangeAngle(angle-20);
                if ( angleNeighbour > range1
                        && angleNeighbour < range2) {
                    continue;
                }
            }

            Node neighbourNode = this.point.nextPosition(compassDirection).toNode();
            neighbourNode.direction = compassDirection;
            legalNeighbours.add(neighbourNode);
        }

        return legalNeighbours;
    }


    /**
     * Checks whether a line between two points intersects with the noFlyZones.
     * @param noFlyZones noFlyZones from REST server.
     * @param finish The second point.
     * @return True if the intersects.
     */
    public boolean intersects(ArrayList<ArrayList<LngLat>> noFlyZones, Node finish) {

        Line2D trajectory = new Line2D.Double();
        LngLat start = this.point;
        LngLat destination = finish.point;
        trajectory.setLine(start.lat(), start.lng(), destination.lat(), destination.lng());

        // Checks if the straight trajectory intersects with any of the noFlyZones.
        boolean intersects = false;
        for (ArrayList<LngLat> noFlyZone : noFlyZones) {
            for (int i = 0; i < noFlyZone.size()-1; i++) {
                intersects = trajectory.intersectsLine(noFlyZone.get(i).lat(), noFlyZone.get(i).lng(),
                        noFlyZone.get(i+1).lat(), noFlyZone.get(i+1).lng());
                if (intersects) {
                    break;
                }
            }
        }
        return intersects;
    }

    /**
     * Gets the angle a line makes.
     * @param destination The other point which creates the line.
     * @return The angle.
     */
    public float getAngleFromLine(Node destination) {
        float angle = (float) Math.toDegrees(Math.atan2(destination.point.lat() - point.lat(),
                destination.point.lng()) - point.lng());

        if(angle < 0){
            angle += 360;
        }
        if(angle > 360){
            angle -= 360;
        }

        return angle;
    }

    /**
     * Helper function used when angles are being added.
     * @param angle The angle.
     * @return Corrected angle.
     */
    public double getRangeAngle(double angle) {

        if (angle > 360) {
            return angle - 360;
        }
        else if (angle < 0) {
            return angle + 360;
        }
        else {
            return angle;
        }
    }

    /**
     * Function that calculates the path the drone will need to make, using A* Algorithm.
     * @param destination Where the drone needs to end up 'close' to.
     * @return A list with all the points (representing the steps)
     */
    public ArrayList<LngLat> findPath(Node destination) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        ArrayList<Node> closedList = new ArrayList<>();
        HashMap<LngLat, Node> all = new HashMap<>();
        Node start = this;

        start.calculateHeuristic(destination);
        start.setG(0.0);
        start.calculateF();
        all.put(start.point, start);

        openList.add(start);

        while(!openList.isEmpty()) {

            // Get node with lowest F
            Node currentNode = openList.peek();

            // Stop if we find the destination node
            if (currentNode.point.closeTo(destination.point)) {
                // Backtrack to get path
                ArrayList<LngLat> path = new ArrayList<>();
                path.add(currentNode.point);
                while (currentNode.parent != null) {
                    LngLat getPoint = currentNode.parent.point;
                    path.add(getPoint);
                    currentNode = currentNode.parent;
                }
                Collections.reverse(path);
                return path;
            }

            // Finds all legal neighbours of current node.
            System.out.println("aqui es donde tarda");
            ArrayList<Node> neighbours = currentNode.findLegalNeighbours(destination);

            for (Node neighbour : neighbours) {

                // If the neighbour can be used
                if (!currentNode.intersects(noFlyZones, neighbour)) {

                    // In order to control if we have found the same node
                    if (all.containsKey(neighbour.point)) {
                        neighbour = all.get(neighbour.point);
                    } else {
                        all.put(neighbour.point, neighbour);
                    }

                    // Calculates G
                    double distanceTravelled = currentNode.G + currentNode.point.distanceTo(neighbour.point);

                    // If either closedList or openList contain the neighbour, check if this path is shorter.
                    if (closedList.contains(neighbour) || openList.contains(neighbour)) {
                        if (distanceTravelled < neighbour.G) {
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
            System.out.println("-----------");
        }
        return null;
        // No path could be found :(
    }

    // GETTERS

    public Double getG() {
        return G;
    }

    public Double getH() {
        return H;
    }

    public void setG(Double g) {
        G = g;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }
}
