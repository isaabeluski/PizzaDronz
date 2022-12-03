package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.geom.Line2D;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Singleton which retrieves REST Server information about the Central Area.
 */

public class CentralAreaClient {

    private final ArrayList<Line2D.Double> centralAreaLines;

    private static CentralAreaClient centralArea;




    private CentralAreaClient() {
        CentralAreaPoint[] centralAreaPoints = centralCoordinates();
        this.centralAreaLines = centralLines(centralAreaPoints);
    }

    /**
     * If an object of this class already exists, it returns it. Otherwise, it creates a new one.
     * @return an instance of CentralAreaClient.
     */
    public static CentralAreaClient getInstance() {
        if (centralArea == null) {
            centralArea = new CentralAreaClient();
        }
        return centralArea;
    }

    /**
     * Retrieves the coordinates from the REST serve of the points that represent the polygon in Central Area.
     *
     * @return A list of the points of the polygon representing the Central Area.
     */
    private CentralAreaPoint[] centralCoordinates() {
        try {
            URL url = new URL("https://ilp-rest.azurewebsites.net/centralArea");

            return new ObjectMapper().readValue(url, CentralAreaPoint[].class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Line2D.Double> centralLines(CentralAreaPoint[] centralAreas) {
        CentralAreaPoint firstPoint = centralAreas[0];
        ArrayList<Line2D.Double> centralAreaLines = new ArrayList<>();
        var cap = new ArrayList<>(Arrays.stream(centralAreas).toList());
        cap.add(firstPoint);

        for (int i = 0; i < cap.size() - 1; i++) {
            Line2D.Double line = new Line2D.Double(
                    cap.get(i).getLng(),
                    cap.get(i).getLat(),
                    cap.get(i+1).getLng(),
                    cap.get(i+1).getLat());
            centralAreaLines.add(line);
        }
        return centralAreaLines;
    }

    public boolean intersectsCentralArea(Node start, Node destination) {
        //build line
        var x = start.getPoint().toPoint();
        var y = destination.getPoint().toPoint();
        Line2D.Double line = new Line2D.Double(x.longitude(), x.latitude(),
                y.longitude(), y.latitude());

        //Check if line intersects with any of the lines in the central area
        for (var centralLine : this.centralAreaLines) {
            if (centralLine.intersectsLine(line) || line.intersectsLine(centralLine)) {
                return true;
            }
        }
        return false;
    }

}

