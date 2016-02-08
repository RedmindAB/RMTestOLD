package se.redmind.rmtest.selenium.livestream.test;

import com.google.gson.*;
import org.junit.Test;
import static org.junit.Assert.*;
import se.redmind.rmtest.selenium.livestream.JsonReportOrganizer;


import java.io.*;

/**
 * Created by victormattsson on 2016-01-26.
 */
public class JsonReportOrganizerTest {

    JsonObject jsonReport;
    JsonObject newJsonObject;
    JsonReportOrganizer organizer;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public JsonReportOrganizerTest(){
        jsonReport = readJsonReport();
        organizer = new JsonReportOrganizer(jsonReport);
    }

    @Test
    public void getTrueTestCount(){
        newJsonObject = organizer.build();
        assertEquals(10, organizer.getTestCount());
    }

    @Test
    public void checkJsonStringLength(){
        newJsonObject = organizer.build();
        String json = gson.toJson(newJsonObject);
        assertEquals(44838, json.length());
    }

    @Test
    public void checkNumberOfGherkinElements(){
        organizer.build();
        assertEquals(26, organizer.getGherkinScenarios().size());
    }

    @Test
    public void checkNumberOfRegularTests(){
        organizer.build();
        assertEquals(4, organizer.getRegularTests().size());
    }

    @Test
    public void checkNumberOfGherkinScenarios(){
        organizer.build();
        assertEquals(6, organizer.getGherkinMap().size());
    }


    private JsonObject readJsonReport() {
        JsonParser parser = new JsonParser();
        JsonElement element = null;
        try {
            element = parser.parse(new FileReader(getClass().getResource("/jsonreports/mixedJsonReport.json").getPath()));
        } catch (FileNotFoundException e) {
            System.err.println("Json report file not located..");
        }
        return element.getAsJsonObject();
    }
}
