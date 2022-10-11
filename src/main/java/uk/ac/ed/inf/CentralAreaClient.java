package uk.ac.ed.inf;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    public List<LngLat> centralCoordinates() {

        try {

            String baseUrl = "https://ilp-rest.azurewebsites.net/centralArea";
            URL url = new URL(baseUrl);

            List<CentralArea> centralAreas = List.of(new ObjectMapper().readValue(url, CentralArea[].class));

            List<LngLat> coordinates = new ArrayList<>();
            for (CentralArea area : centralAreas) {
                LngLat coord = new LngLat(area.getLng(), area.getLat());
                coordinates.add(coord);
            }

            // Adds the first point at the end
            coordinates.add(new LngLat(centralAreas.get(0).getLng(), centralAreas.get(0).getLng()));

            return coordinates;
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
