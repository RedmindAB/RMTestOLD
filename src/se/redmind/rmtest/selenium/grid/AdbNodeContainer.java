package se.redmind.rmtest.selenium.grid;

public class AdbNodeContainer {
    private String port;
    private String version;
    private String model;
    private String id;
    public AdbNodeContainer(String nodeString) {
        String[] node;
        node = nodeString.split(":");
        if (node.length >= 4) {
            port = node[0];
            id = node[1];
            model = node[2];
            version = node[3];
        } else {
            System.err.println("This node is not parsable, contains to few elements!");
        }
    }
    public String getPort() {
        return port;
    }

    public String getVersion() {
        return version;
    }

    public String getModel() {
        return model;
    }

    public String getId() {
        return id;
    }
    
    public String toString() {
        String concatenatedString = port + ":" + id + ":" + model + ":" + version;
        return concatenatedString;
    }
}
