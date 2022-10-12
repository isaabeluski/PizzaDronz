package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class CentralAreaClient {

    private static CentralAreaClient centralArea;

    private CentralAreaClient() {
    }

    public static CentralAreaClient getInstance() {
        if (centralArea == null) {
            centralArea = new CentralAreaClient();
        }
        return centralArea;
    }

    public LngLat[] centralCoordinates() {

        try {

            String baseUrl = "https://ilp-rest.azurewebsites.net/centralArea";
            URL url = new URL(baseUrl);

            CentralAreaPoint[] centralAreas = new ObjectMapper().readValue(url, CentralAreaPoint[].class);

            LngLat[] coordinates = new LngLat[centralAreas.length];
            int i = 0;
            for (CentralAreaPoint point : centralAreas) {
                LngLat coord = new LngLat(point.getLng(), point.getLat());
                coordinates[i] = coord;
                i++;
            }

            // Adds the first point at the end

            //coordinates.set(3, new LngLat(-3.1843194291422,55.9464023239621));

            return coordinates;
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
