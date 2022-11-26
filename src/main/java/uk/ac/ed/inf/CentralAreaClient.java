package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

/**
 * Singleton which retrieves REST Server information about the Central Area.
 */

public class CentralAreaClient {

    private static CentralAreaClient centralArea;
    private static LngLat[] centralCoordinates;



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
    public LngLat[] centralCoordinates() {
        try {
            URL url = new URL("https://ilp-rest.azurewebsites.net/centralArea");

            CentralAreaPoint[] centralAreas = new ObjectMapper().readValue(url, CentralAreaPoint[].class);

            // Converts the Central Areas points into LngLat objects.
            LngLat[] coordinates = new LngLat[centralAreas.length];
            int i = 0;
            for (CentralAreaPoint point : centralAreas) {
                coordinates[i] = new LngLat(point.getLng(), point.getLat());
                i++;
            }

            return coordinates;
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets list of points which represent the coordinates of Central Area.
     * Additionally, if no centralCoordinates have been assigned, it calls the REST server to assign them.
     * @return Central Area points.
     */
    public static LngLat[] getCentralCoordinates() {
        if (centralCoordinates == null) {
            centralCoordinates = getInstance().centralCoordinates();
        }
        return centralCoordinates;
    }
}
