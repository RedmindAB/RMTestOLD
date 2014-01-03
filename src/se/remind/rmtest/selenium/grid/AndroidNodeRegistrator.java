package se.remind.rmtest.selenium.grid;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.grid.common.GridRole;
import org.openqa.grid.common.RegistrationRequest;
import org.openqa.grid.internal.utils.SelfRegisteringRemote;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AndroidNodeRegistrator extends RegistrationRequest {

    public void sendRegistrationReq(){
        SelfRegisteringRemote remote = new SelfRegisteringRemote(this);
//        try {
//            remote.startRemoteServer();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        remote.startRegistrationProcess();
    }
    
    public void regNodeInfo(String hubHost, String nodeHost, AdbNodeContainer adbNode) throws Exception{
        // TODO Auto-generated method stub
        
        this.setRole(GridRole.NODE);
        this.addDesiredCapability(new DesiredCapabilities("android", adbNode.getVersion(), Platform.ANDROID));
        this.setDescription(adbNode.getModel() + " Version: " + adbNode.getVersion() +  " id: " + adbNode.getId());
//        req.setCapabilities();
        Map<String, Object> nodeConfiguration = new HashMap<String,
        Object>();
        
        nodeConfiguration.put(RegistrationRequest.AUTO_REGISTER, true);
        nodeConfiguration.put(RegistrationRequest.HUB_HOST, hubHost);

        nodeConfiguration.put(RegistrationRequest.HUB_PORT, 4444);
        nodeConfiguration.put(RegistrationRequest.PORT, adbNode.getPort());
        nodeConfiguration.put(RegistrationRequest.HOST, nodeHost);

        URL remoteURL = new URL("http://" + nodeHost + ":" + adbNode.getPort());
        nodeConfiguration.put(RegistrationRequest.PROXY_CLASS, "org.openqa.grid.selenium.proxy.DefaultRemoteProxy");
        nodeConfiguration.put(RegistrationRequest.MAX_SESSION, 1);
        nodeConfiguration.put(RegistrationRequest.CLEAN_UP_CYCLE, 2222);
        nodeConfiguration.put(RegistrationRequest.REMOTE_HOST, remoteURL);
        nodeConfiguration.put(RegistrationRequest.MAX_INSTANCES, 1);
        

        this.setConfiguration(nodeConfiguration);
        
        
        
    }

}
