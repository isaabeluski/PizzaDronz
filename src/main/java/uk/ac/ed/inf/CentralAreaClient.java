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
    private String baseUrl = "https://ilp-rest.azurewebsites.net/";
    private String endUrl = "centralArea";

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

            if (!baseUrl.endsWith("/")) {
                baseUrl += "/";
            }

            URL url = new URL(baseUrl + endUrl);

            List<CentralArea> centralAreas = List.of(new ObjectMapper().readValue(url, CentralArea[].class));

            List<LngLat> coordinates = new ArrayList<LngLat>();
            for (CentralArea area : centralAreas) {
                LngLat coord = new LngLat(area.getLongitude(), area.getLatitude());
                coordinates.add(coord);
            }

            coordinates.add(new LngLat(centralAreas.get(0).getLongitude(), centralAreas.get(0).getLongitude()));

            return coordinates;
            
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (StreamReadException e) {
            throw new RuntimeException(e);
        } catch (DatabindException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
