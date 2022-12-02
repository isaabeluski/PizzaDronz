package uk.ac.ed.inf;

import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Node implements Comparable<Node>{

    private final LngLat point;
    private Double F;
    private Double G = 0.0;
    private Double H = 0.0;
    private Node parent;
    private Compass direction;

    public Node(LngLat point)  {
        this.point = point;
    }

    public void calculateF() {
        this.F = 0.9*this.G + this.H;
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
     * @return A list of nodes representing the possible next position.
     */
    public ArrayList<Node> findNeighbours() {
        ArrayList<Node> legalNeighbours = new ArrayList<>();
        Compass[] values = Compass.class.getEnumConstants();
        for (Compass compassDirection : values) {
            Node neighbourNode = this.nextPosition(compassDirection);
            neighbourNode.direction = compassDirection;
            legalNeighbours.add(neighbourNode);
        }
        return legalNeighbours;
    }



    public Node nextPosition(Compass direction) {
        return this.point.nextPosition(direction).toNode();
    }

    public boolean closeTo(Node node) {
        return this.point.distanceTo(node.point) < 0.00015;
    }



    // GETTERS AND SETTERS

    public Double getG() {
        return G;
    }

    public Double getH() {
        return H;
    }

    public void setG(Double g) {
        G = g;
    }

    public Node getParent() {
        return parent;
    }

    public LngLat getPoint() {
        return point;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Compass getDirection() {
        return direction;
    }
}
