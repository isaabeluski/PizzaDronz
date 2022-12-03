package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapbox.geojson.Point;
import com.mapbox.geojson.Polygon;

import java.io.IOException;
import java.net.URL;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

/**
 * Singleton which retrieves REST Server information about the Central Area.
 */

public class CentralAreaClient {

    private static CentralAreaClient centralArea;
    private static Polygon centralCoordinates;



    private CentralAreaClient() {
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
     * @return A list of the points of the polygon representing the Central Area.
     */
    public Polygon centralCoordinates() {
        try {
            URL url = new URL("https://ilp-rest.azurewebsites.net/centralArea");

            CentralAreaPoint[] centralAreas = new ObjectMapper().readValue(url, CentralAreaPoint[].class);

            // Converts the Central Areas points into LngLat objects.
            List<Point> points = new ArrayList<>();
            List<List<Point>> allPoints = new ArrayList<>();
            CentralAreaPoint firstPoint = centralAreas[0];
            for (CentralAreaPoint point : centralAreas) {
                points.add(Point.fromLngLat(point.getLng(), point.getLat()));
            }
            points.add(Point.fromLngLat(firstPoint.getLng(), firstPoint.getLat()));
            allPoints.add(points);
            Polygon polygon = Polygon.fromLngLats(allPoints);
            System.out.println(polygon);
            return polygon;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets list of points which represent the coordinates of Central Area.
     * Additionally, if no centralCoordinates have been assigned, it calls the REST server to assign them.
     * @return Central Area points.
     */
    public static Polygon getCentralCoordinates() {
        if (centralCoordinates == null) {
            centralCoordinates = getInstance().centralCoordinates();
        }
        return centralCoordinates;
    }
}


