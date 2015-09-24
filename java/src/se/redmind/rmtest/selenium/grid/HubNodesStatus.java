package se.redmind.rmtest.selenium.grid;

import java.util.ArrayList;
import java.util.Map;

import org.openqa.grid.common.RegistrationRequest;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.JsonToBeanConverter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HubNodesStatus {

	private JsonArray nodesAsJson;
	private ArrayList<RegistrationRequest> nodesAsRegReqs = new ArrayList<RegistrationRequest>();
	// private static RegistrationRequest regReq = new RegistrationRequest();

	public HubNodesStatus(String pHost, int pPort) {
		try {
			nodesAsJson = NodeInfoFromHub.main(pHost, pPort).getAsJsonArray("FreeProxies");
			RegistrationRequest currentNode = new RegistrationRequest();
			for (int i = 0; i < nodesAsJson.size(); i++) {
				System.out.println(nodesAsJson.get(i).getAsJsonObject().toString());

				currentNode = getRegRequest(nodesAsJson.get(i).getAsJsonObject());
				// System.out.println(currentNode.getConfigAsString("port"));
				nodesAsRegReqs.add(currentNode);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static RegistrationRequest getRegRequest(JsonObject node) {
		RegistrationRequest request = new RegistrationRequest();

		JsonObject config = node.get("configuration").getAsJsonObject();
		Map<String, Object> configuration = new JsonToBeanConverter().convert(Map.class, config);
		// For backward compatibility numbers should be converted to integers
		for (String key : configuration.keySet()) {
			Object value = configuration.get(key);
			if (value instanceof Long) {
				configuration.put(key, ((Long) value).intValue());
			}
		}
		request.setConfiguration(configuration);

		JsonArray capabilities = node.get("capabilities").getAsJsonArray();

		for (int i = 0; i < capabilities.size(); i++) {
			DesiredCapabilities cap = new JsonToBeanConverter().convert(DesiredCapabilities.class, capabilities.get(i));
			request.addDesiredCapability(cap);
		}
		return request;

	}
	// public JsonArray getNodesAsJson(){
	// return nodesAsJson;
	// }

	public ArrayList<RegistrationRequest> getNodesAsRegReqs() {
		return nodesAsRegReqs;
	}

}