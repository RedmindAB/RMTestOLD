package se.redmind.rmtest.selenium.livestream;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class RmTestResultBuilder {

    private static final String FAILURE_MESSAGE = "failureMessage";
    public static final String RESULT = "result";
    private final TreeMap<String, JsonObject> testMap;
    private String suiteName;
    private int totalTests;
    private final String uid;
    private final String timestamp;
    private int totalFail;
    private int totalIgnored;
    private double runTime;
    private boolean success;

    public RmTestResultBuilder() {
        this.testMap = new TreeMap<>();
        this.totalTests = 0;
        this.uid = UUID.randomUUID().toString();
        this.timestamp = formattedTimestamp();
    }

    public String getSuiteName() {
        return suiteName;
    }

    public void setSuiteName(String suiteName) {
        this.suiteName = suiteName;
    }

    public void addTest(String displayName) {
        if (!testMap.containsKey(displayName)) {
            JsonElement deviceInfo = getDeviceInfo(displayName);
            if (deviceInfo != JsonNull.INSTANCE) {
                totalTests++;
                JsonObject test = new JsonObject();
                test.addProperty("id", totalTests);
                test.addProperty("method", getMethodName(displayName));
                test.addProperty("testclass", getTestClass(displayName));
                test.addProperty("status", "idle");
                test.add(RESULT, JsonNull.INSTANCE);
                test.add("deviceInfo", deviceInfo);
                testMap.put(displayName, test);
            }
        }
    }

    private String getTestClass(String displayName) {
        int start = displayName.indexOf('(');
        int end = displayName.lastIndexOf(')');
        String testClass = displayName.substring(start + 1, end);
        return testClass;
    }

    private JsonElement getDeviceInfo(String displayName) {
        int start = displayName.indexOf('[');
        int end = displayName.lastIndexOf(']');
        String deviceInfoString = displayName.substring(start + 1, end);
        return extractDeviceInfo(deviceInfoString);

    }

    private JsonElement extractDeviceInfo(String deviceInfo) {
        String[] info = deviceInfo.split("_");
        JsonObject deviceInfoObject = new JsonObject();
        if (info.length == 5) {
            deviceInfoObject.addProperty("os", info[0]);
            deviceInfoObject.addProperty("osver", info[1]);
            deviceInfoObject.addProperty("device", info[2]);
            deviceInfoObject.addProperty("browser", info[3]);
            deviceInfoObject.addProperty("browserVer", info[4]);
        } else {

            return JsonNull.INSTANCE;
        }
        return deviceInfoObject;
    }

    private String getMethodName(String displayName) {
        return displayName.substring(0, displayName.lastIndexOf('['));
    }

    public JsonObject build() {
        JsonObject buildObj = new JsonObject();
        buildObj.addProperty("suite", suiteName);
        buildObj.addProperty("totalTests", totalTests);
        buildObj.addProperty("failures", totalFail);
        buildObj.addProperty("totalIgnored", totalIgnored);
        buildObj.addProperty("success", success);
        buildObj.addProperty("runTime", runTime);
        buildObj.addProperty("UUID", this.uid);
        buildObj.addProperty("timestamp", timestamp);
        Set<String> keySet = testMap.keySet();
        JsonArray tests = new JsonArray();
        keySet.stream().map(key -> testMap.get(key)).forEach(test -> tests.add(test));
        buildObj.add("tests", tests);
        buildObj.add("properties", getSystemProperties());
        return buildObj;
    }

    private JsonElement getSystemProperties() {
        JsonObject systemProperties = new JsonObject();
        Properties properties = System.getProperties();
        Set<Object> propertiesKeySet = properties.keySet();
        for (Object propertyKey : propertiesKeySet) {
            systemProperties.addProperty((String) propertyKey, System.getProperty((String) propertyKey));
        }
        return systemProperties;
    }

    public JsonObject getTest(String displayName) {
        return testMap.get(displayName);
    }

    @Override
    public String toString() {
        return build().toString();
    }

    public void addAssumptionFailure(String description, Failure failure) {
        JsonObject test = testMap.get(description);
        test.addProperty(RESULT, "skipped");
        test.addProperty(FAILURE_MESSAGE, failure.getTrace());
    }

    public void addTestFailure(String description, Failure failure) {
        JsonObject test = testMap.get(description);
        if (test != null) {
            test.addProperty(RESULT, "failure");
            test.addProperty(FAILURE_MESSAGE, failure.getTrace());
        }
    }

    public void addIgnoredTest(String displayName) {
        JsonObject test = testMap.get(displayName);
        if (test != null) {
            test.addProperty("runTime", 0);
            test.addProperty(RESULT, "skipped");
        }
    }

    public void addFinishedTest(String displayName) {
        JsonObject test = testMap.get(displayName);
        if (test != null) {
            if (test.get(RESULT) instanceof JsonNull) {
                test.addProperty(RESULT, "passed");
            }
        }
    }

    public void addRunTime(String dispName, double time) {
        JsonObject test = testMap.get(dispName);
        if (test != null) {
            test.addProperty("runTime", time);
        }
    }

    private String formattedTimestamp() {
        String surefireTimestamp = System.getProperty("rmt.timestamp");
        if (surefireTimestamp != null) {
            surefireTimestamp = surefireTimestamp.replaceAll("-", "");
            return surefireTimestamp;
        } else {
            SimpleDateFormat form = new SimpleDateFormat("yyyyMMddHHmmss");
            return form.format(new Date());
        }
    }

    public void setResult(Result result) {
        this.totalFail = result.getFailureCount();
        this.totalIgnored = result.getIgnoreCount();
        this.runTime = (double) result.getRunTime() / 1000;
        this.success = result.wasSuccessful();
    }

    public String getTimestamp() {
        return timestamp;
    }
}
