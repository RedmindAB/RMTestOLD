package se.remind.rmtest.selenium.grid;



import java.util.ArrayList;

import org.json.JSONArray;
import org.openqa.grid.common.RegistrationRequest;

public class HubNodesStatus {

    private JSONArray nodesAsJson;
    private ArrayList <RegistrationRequest> nodesAsRegReqs = new ArrayList<RegistrationRequest>();
//    private static RegistrationRequest regReq = new RegistrationRequest();

    public  HubNodesStatus(String pHost, int pPort) {
        try {
            nodesAsJson = NodeInfoFromHub.main(pHost, pPort).getJSONArray("FreeProxies");
            RegistrationRequest currentNode = new RegistrationRequest();
            for (int i = 0; i < nodesAsJson.length(); i++) {
                
                currentNode = RegistrationRequest.getNewInstance(nodesAsJson.getJSONObject(i).toString());
//                System.out.println(currentNode.getConfigAsString("port"));
                nodesAsRegReqs.add(currentNode);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }

    public JSONArray getNodesAsJson(){
        return nodesAsJson;
    }

    public ArrayList<RegistrationRequest> getNodesAsRegReqs() {
        return nodesAsRegReqs;
    }    
    


}