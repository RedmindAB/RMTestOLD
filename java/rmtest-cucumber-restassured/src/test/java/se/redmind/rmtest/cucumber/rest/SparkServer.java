package se.redmind.rmtest.cucumber.rest;

import static spark.Spark.*;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import se.redmind.utils.Fields;
import spark.Spark;
import spark.webserver.JettySparkServer;

public class SparkServer {

    public void init() {
        init();
    }

    Gson gson = new Gson();
	public int localPort;

    public SparkServer initServices() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
    	Spark.port(0);
        //root
        get("/", (req, res) -> "hello");
        //status
        get("/status/:status", (req, res) -> {
            int code = Integer.valueOf(req.params("status"));
            res.status(code);
            return "";
        });

        //json
        get("/json", (req, res) -> gson.fromJson(req.body(), JsonElement.class));
        get("/param", (req, res) -> {
            JsonObject json = new JsonObject();
            req.queryParams().forEach(key -> json.addProperty(key, req.queryParams(key)));
            return json;

        });

        //Filters
        after("/json", (req, res) -> res.header("Content-Type", "application/json"));
        after("/param", (req, res) -> res.header("Content-Type", "application/json"));
        awaitInitialization();
        Spark.awaitInitialization();
        JettySparkServer sparkServer = Fields.getValue(Spark.getInstance(), "server");
        Server jettyServer = Fields.getValue(sparkServer, "server");
        ServerConnector connector = (ServerConnector) jettyServer.getConnectors()[0];
        localPort = connector.getLocalPort();
        return this;
    }
    
    public int getLocalPort(){
    	return localPort;
    }
}
