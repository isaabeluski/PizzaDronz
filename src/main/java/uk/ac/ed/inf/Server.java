package uk.ac.ed.inf;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.UrlValidator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Server {

    private static class ServerTest {
        @JsonProperty( "greeting" )
        public String greeting ;
    }

    private String date;
    private String baseUrl;
    private static Server server;

    private Server() {
    }

    public static Server getInstance() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }

    public void setBaseUrl(String baseUrl) {
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        this.baseUrl = baseUrl;
    }

    public void validUrl(String url) {
        String[] schemes = {"http", "https", "ftp", "file","jar"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        if (!urlValidator.isValid(url)) {
            System.out.println("Invalid URL");
            System.exit(1);
        }

        if (!url.endsWith("/")) {
            url += "/";
        }

        // Test with an endpoint
        String testUrl = url + "test/test";
        try {
            URL test = new URL(testUrl);
            ServerTest serverTest = new ObjectMapper().readValue(test, ServerTest.class);

            if (!serverTest.greeting.endsWith("test")) {
                System.out.println("Test failed");
                System.exit(1);
            }

        } catch (IOException e) {
            System.out.println("Invalid URL");
            System.exit(1);
        }
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
