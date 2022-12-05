package uk.ac.ed.inf;

import java.util.ArrayList;

public class Node implements Comparable<Node>{

    private final LngLat point;
    private Double F;
    private Double G = 0.0;
    private Double H = 0.0;
    private Node parent;
    private long ticks;

    private Double angle = null;

    private int intersectionsCentralArea = 0;
    private static final CentralArea centralArea = CentralArea.getInstance();
    private static final NoFlyZones noFlyZones = NoFlyZones.getInstance();


    /**
     * Constructor.
     * @param point The LngLat point of the node.
     */
    public Node(LngLat point)  {
        this.point = point;
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
     * Calculates the F score of the node.
     */
    public void calculateF() {
        this.F = 0.9*this.G + this.H;
    }

    /**
     * Calculates the heuristic for the A* algorithm, where the Euclidean distance is used.
     * @param destination A node representing the final destination of a trip.
     */
    public void calculateHeuristic(Node destination) {
        this.H = this.point.distanceTo(destination.point);
    }

    /**
     * Gets all possible legal positions the drone can go to from the current node. Therefore, it checks
     * whether the no-fly-zones are being intersected and whether the drone leaves/enters the central area more
     * than once.
     * @return A list of nodes representing the possible legal next positions.
     */
    public ArrayList<Node> findNeighbours() {
        ArrayList<Node> legalNeighbours = new ArrayList<>();
        Compass[] values = Compass.class.getEnumConstants();
        for (Compass compassDirection : values) {
            Node neighbourNode = this.nextPosition(compassDirection);

            // Checks that line between current node and neighbour does not intersect with any no-fly zones.
            if (noFlyZones.intersectsNoFlyZone(this, neighbourNode)) {
                continue;
            }

            // Checks that line between current node and neighbour does not leave/enter central area more than once.
            if (centralArea.intersectsCentralArea(this, neighbourNode)) {
                var copy = this.intersectionsCentralArea;

                if (centralArea.isInsideCentralArea(this) == centralArea.isInsideCentralArea(neighbourNode)) {
                    continue;
                }

                this.intersectionsCentralArea++;
                if (neighbourNode.intersectionsCentralArea > 1) {
                    this.intersectionsCentralArea = copy;
                    continue;
                }
            }

            neighbourNode.angle = compassDirection.getAngle();
            neighbourNode.intersectionsCentralArea = this.intersectionsCentralArea;
            legalNeighbours.add(neighbourNode);

        }
        return legalNeighbours;
    }


    /**
     * Calculates the next position of the drone with nodes.
     * @param direction A compass direction.
     * @return A node in the next position.
     */
    public Node nextPosition(Compass direction) {
        return this.point.nextPosition(direction).toNode();
    }


    // GETTERS

    public Double getG() {
        return G;
    }

    public Double getH() {
        return H;
    }

    public Node getParent() {
        return parent;
    }

    public LngLat getPoint() {
        return point;
    }

    public long getTicks() {
        return ticks;
    }

    public Double getAngle() {
        return angle;
    }

    // SETTERS
    public void setG(Double g) {
        G = g;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }


    public void setTicks(long ticks) {
        this.ticks = ticks;
    }

    public void setNull() {
        this.angle = null;
    }

    public int getIntersectionsCentralArea() {
        return intersectionsCentralArea;
    }

    public void increaseIntersectionsCA() {
        this.intersectionsCentralArea += 1;
    }
}
