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
     * Gets all possible positions the drone can go to from the current node.
     * @return A list of nodes representing the possible next position.
     */
    public ArrayList<Node> findNeighbours() {
        ArrayList<Node> legalNeighbours = new ArrayList<>();
        Compass[] values = Compass.class.getEnumConstants();
        for (Compass compassDirection : values) {
            Node neighbourNode = this.nextPosition(compassDirection);
            neighbourNode.angle = compassDirection.getAngle();
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

}
