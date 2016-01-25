package se.redmind.rmtest.cucumber.rest;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SparkServer {

    public void init() {
        init();
    }

    Gson gson = new Gson();

    public void initServices() {
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
    }
}
