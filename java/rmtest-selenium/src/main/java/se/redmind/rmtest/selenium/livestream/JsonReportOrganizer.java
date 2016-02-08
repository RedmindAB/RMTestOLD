package se.redmind.rmtest.selenium.livestream;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class JsonReportOrganizer {

    private JsonObject build;
    private int regularTestCnt = 0;
    private List<JsonElement> gherkinScenarios;
    private List<JsonElement> regularTests;
    private HashMap<String, ArrayList<JsonObject>> gherkinMap;

    public JsonReportOrganizer(JsonObject build) {
        this.build = build;
        gherkinScenarios = new ArrayList<>();
        regularTests = new ArrayList<>();
        gherkinMap = new HashMap<>();
    }

    public JsonObject build() {
        JsonArray tests = sortArrayById(build.get("tests").getAsJsonArray());
        for (JsonElement entry : tests) {
            JsonObject test = entry.getAsJsonObject();
            if (test.get("isGherkin").getAsBoolean()) {
                gherkinScenarios.add(test);
            } else {
                regularTests.add(test);
                regularTestCnt++;
            }
        }
        populateGherkinMap();
        build.addProperty("totalTests", getTestCount());
        build.add("tests", parseToGherkinFormat());
        return build;
    }


    private JsonArray parseToGherkinFormat() {
        JsonArray gherkin = new JsonArray();
        double runTime;

        for (ArrayList<JsonObject> jsonMapObjects : gherkinMap.values()) {
            runTime = 0.0;
            JsonObject testScenario = new JsonObject();
            JsonArray stepObjects = new JsonArray();
            JsonObject step = new JsonObject();
            stepObjects.add(step);
            for (int i = 0; i < jsonMapObjects.size(); i++) {
                if (i == 0) {
                    testScenario = jsonMapObjects.get(i);
                }
                if (testNotPassed(jsonMapObjects, i)) {
                    testScenario.addProperty("result", "failure");
                    if (jsonMapObjects.get(i).get("failureMessage") != null)
                    testScenario.addProperty("failureMessage", jsonMapObjects.get(i).get("failureMessage")
                            .getAsString());
                }
                step.addProperty(String.valueOf(i + 1), jsonMapObjects.get(i).get("method").getAsString());
                runTime += jsonMapObjects.get(i).get("runTime").getAsDouble();

            }
            testScenario.addProperty("runTime", runTime);
            testScenario.addProperty("method", testScenario.get("testclass").getAsString());
            testScenario.addProperty("testclass", testScenario.get("feature").getAsString());
            testScenario.add("steps", stepObjects);
            gherkin.add(testScenario);
        }
        /* Also add the regular tests */
        regularTests.forEach(gherkin::add);
        return gherkin;
    }

    private boolean testNotPassed(ArrayList<JsonObject> objects, int i) {
        return !objects.get(i).get("result").getAsString().equals("passed");
    }

    private int populateGherkinMap() {
        for (JsonElement element : gherkinScenarios) {
            String key = getKey(element);
            if (gherkinMap.containsKey(key)) {
                ArrayList<JsonObject> steps = gherkinMap.get(key);
                steps.add((JsonObject) element);
                gherkinMap.put(key, steps);
            } else {
                ArrayList<JsonObject> steps = new ArrayList<>();
                steps.add((JsonObject) element);
                gherkinMap.put(key, steps);
            }
        }
        return gherkinMap.size();
    }

    private String getKey(JsonElement element) {
        String feature = element.getAsJsonObject().get("feature").getAsString();
        String scenario = element.getAsJsonObject().get("testclass").getAsString();
        String deviceInfo = element.getAsJsonObject().get("deviceInfo").toString();
        return feature + ", " + scenario + ", " + deviceInfo;
    }

    private JsonArray sortArrayById(JsonArray tests) {
        JsonArray sortedArray = new JsonArray();
        List<JsonElement> jsonElements = new ArrayList<>();
        for (JsonElement element : tests) {
            jsonElements.add(element);
        }
        Collections.sort(jsonElements, (o1, o2) -> {
            Integer firstId = o1.getAsJsonObject().get("id").getAsInt();
            Integer secondId = o2.getAsJsonObject().get("id").getAsInt();
            return firstId.compareTo(secondId);
        });
        jsonElements.forEach(sortedArray::add);
        return sortedArray;
    }

    private void printTests() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println("------ GHERKIN TESTS ------");
        for (ArrayList<JsonObject> jsonObjects : gherkinMap.values()) {
            System.out.println("\n--- NEW JSON OBJECT ---\n");
            String json = gson.toJson(jsonObjects);
            System.out.println(json);
        }
        System.out.println("------ REGULAR TESTS ------");
        for (JsonElement test : regularTests) {
            String json = gson.toJson(test);
            System.out.println(json);
        }
    }

    public int getTestCount() {
        int gherkinScenarios = getGherkinCount();
        return regularTestCnt + gherkinScenarios;
    }

    private int getGherkinCount() {
        return gherkinMap.size();
    }

    public List<JsonElement> getGherkinScenarios() {
        return gherkinScenarios;
    }

    public List<JsonElement> getRegularTests() {
        return regularTests;
    }

    public HashMap<String, ArrayList<JsonObject>> getGherkinMap() {
        return gherkinMap;
    }
}
