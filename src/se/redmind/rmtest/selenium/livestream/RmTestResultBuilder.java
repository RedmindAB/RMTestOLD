package se.redmind.rmtest.selenium.livestream;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class RmTestResultBuilder {

	private static final String FAILURE_MESSAGE = "failureMessage";
	private static final String RESULT = "result";
	private TreeMap<String, JsonObject> testMap;
	private String suiteName;
	private int totalTests;
	private List<String> driverDescriptions;
	private final String uid;
	private String timestamp;
	
	
	public RmTestResultBuilder() {
		this.testMap = new TreeMap<String, JsonObject>();
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
			totalTests++;
			JsonObject test = new JsonObject();
			test.addProperty("id", totalTests);
			test.addProperty("method", getMethodName(displayName));
			test.addProperty("testclass", getTestClass(displayName));
			test.add(RESULT, JsonNull.INSTANCE);
			test.add("deviceInfo", getDeviceInfo(displayName));
			testMap.put(displayName, test);
		}
	}

	private String getTestClass(String displayName) {
		int start = displayName.indexOf('(');
		int end = displayName.lastIndexOf(')');
		String testClass = displayName.substring(start+1, end);
		return testClass;
	}

	private JsonElement getDeviceInfo(String displayName) {
		int start = displayName.indexOf('[');
		int end = displayName.lastIndexOf(']');
		String deviceInfoString = displayName.substring(start+1, end);
		System.out.println(deviceInfoString);
		return extractDeviceInfo(deviceInfoString);
	}
	
	private JsonElement extractDeviceInfo(String deviceInfo){
		String[] info = deviceInfo.split("_");
		JsonObject deviceInfoObject = new JsonObject();
		deviceInfoObject.addProperty("os", info[0]);
		deviceInfoObject.addProperty("osver", info[1]);
		deviceInfoObject.addProperty("device", info[2]);
		deviceInfoObject.addProperty("browser", info[3]);
		deviceInfoObject.addProperty("browserVer", info[4]);
		return deviceInfoObject;
	}

	private String getMethodName(String displayName) {
		return displayName.substring(0, displayName.lastIndexOf('['));
	}
	
	public JsonObject build(){
		JsonObject buildObj = new JsonObject();
		buildObj.addProperty("suite", suiteName);
		buildObj.addProperty("totalTests", totalTests);
		buildObj.addProperty("UUID", this.uid);
		buildObj.addProperty("timestamp", timestamp);
		Set<String> keySet = testMap.keySet();
		JsonObject tests = new JsonObject();
		for (String key : keySet) {
			JsonObject test = testMap.get(key);
			tests.add(test.get("id").getAsString(),test);
		}
		buildObj.add("tests", tests);
		return buildObj;
	}
	
	public JsonObject getTest(String displayName) {
		return testMap.get(displayName);
	}
	
	@Override
	public String toString(){
		return build().toString();
	}


	public void addAssumptionFailure(String description, Failure failure) {
		JsonObject test = testMap.get(description);
		test.addProperty(RESULT, "failure");
		test.addProperty(FAILURE_MESSAGE, failure.getTrace());
	}


	public void addTestFailure(String description, Failure failure) {
		JsonObject test = testMap.get(description);
		test.addProperty(RESULT, "failure");
		test.addProperty(FAILURE_MESSAGE, failure.getTrace());
	}


	public void addIgnoredTest(String displayName) {
		JsonObject test = testMap.get(displayName);
		test.addProperty(RESULT, "skipped");
	}


	public void addFinishedTest(String displayName) {
		JsonObject test = testMap.get(displayName);
		if (test.get(RESULT) instanceof JsonNull) {
			test.addProperty(RESULT, "passed");
		}
	}
	
	private String formattedTimestamp(){
		SimpleDateFormat form = new SimpleDateFormat("yyyyMMddHHmmss");
		return form.format(new Date());
	}
}
