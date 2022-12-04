package uk.ac.ed.inf;

public class Server {

    String date;
    String baseUrl;
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
