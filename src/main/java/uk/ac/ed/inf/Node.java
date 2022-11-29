package uk.ac.ed.inf;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

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
    public ArrayList<Node> findLegalNeighbours() {
        ArrayList<Node> legalNeighbours = new ArrayList<>();
        Compass[] values = Compass.class.getEnumConstants();
        for (Compass compassDirection : values) {
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
    public boolean intersects(Node finish, ArrayList<ArrayList<LngLat>> noFlyZones) {
        // TODO: Move this to noFlyZone and do singleton.
        Line2D.Double trajectory = new Line2D.Double();
        LngLat start = this.getPoint();
        LngLat destination = finish.getPoint();
        trajectory.setLine(start.lng(), start.lat(), destination.lng(), destination.lat());

        // Checks if the straight trajectory intersects with any of the noFlyZones.
        //boolean intersects = false;
        for (ArrayList<LngLat> noFlyZone : noFlyZones) {
            for (int i = 0; i < noFlyZone.size()-1; i++) {
                Line2D.Double zoneLine = new Line2D.Double(noFlyZone.get(i).lng(),
                        noFlyZone.get(i).lat(),
                        noFlyZone.get(i+1).lng(),
                        noFlyZone.get(i+1).lat());
                if (trajectory.intersectsLine(zoneLine) || zoneLine.intersectsLine(trajectory)) {
                    return true;
                }
            }
        }
        return false;
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

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public LngLat getPoint() {
        return point;
    }

}
