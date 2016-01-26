package se.redmind.rmtest.selenium.livestream;

import com.google.gson.JsonObject;

public class JsonReportOrganizer {

	private JsonObject build;

	public JsonReportOrganizer(JsonObject build) {
		this.build = build;
	}

	public JsonObject build(){
		return build;
	}
	
}
