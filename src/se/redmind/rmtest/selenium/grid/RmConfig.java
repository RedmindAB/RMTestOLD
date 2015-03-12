package se.redmind.rmtest.selenium.grid;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.client.ClientProtocolException;
//import org.json.JSONException;
//import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
 
public class RmConfig {

static 	String configFile = TestHome.main() + "/etc/LocalConfig.json";

static JsonObject config;
	
    public  RmConfig() {
    	InputStream fis = null;
		try {
			fis = new FileInputStream(configFile);
	
        BufferedReader br = new BufferedReader(new InputStreamReader(fis)); 
        StringBuilder s = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            s.append(line);
          }
        br.close();
        
        config = new Gson().fromJson(s.toString(), JsonObject.class);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
    }

    public static String getConfigValue(String ConfigKey) {
    	String configValue = null;
    	System.out.println(config.toString());
		
			configValue = config.getAsJsonObject("configuration").get(ConfigKey).getAsString();
	
		
		return configValue;
    }
    
//    public static String getTestHome() {
//		return getConfigValue("testHome");
//	}
    
    public static String getAndroidHome() {
 		return getConfigValue("androidHome");
 	}
    
	public static String getHubIp() {
		return getConfigValue("hubIp");
	}
	
	public static String getLocalIp() {
		return getConfigValue("localIp");
	}
	
	public static String getBuildToolsVersion() {
		return getConfigValue("AndroidBuildtoolsVersion");
	}
 
    }