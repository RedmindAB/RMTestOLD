package se.redmind.rmtest.cucumber.rest;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SparkServer {
	
	public void init(){
		init();
	}
	
	Gson gson =  new Gson();
	
	public void initServices(){
		after("/json", (req,res) -> res.header("Content-Type", "application/json"));
		//root
		get("/",(req,res) -> "hello");
		//json
		get("/json",(req, res) -> gson.fromJson(req.body(), JsonElement.class));
	}
}
