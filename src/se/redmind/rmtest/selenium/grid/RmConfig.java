package se.redmind.rmtest.selenium.grid;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class RmConfig {

	static String localConfigFile = TestHome.main() + "/etc/LocalConfig.json";
	static String rmConfigFile = TestHome.main() + "/etc/RmConfig.json";
	
	static JsonObject localConfig;
	static JsonObject rmConfig;

	public RmConfig() {

		rmConfig = appendFileToConfig(rmConfigFile);
		localConfig = appendFileToConfig(localConfigFile);

		

	}

	/**
	 * @param s
	 */
	private JsonObject appendFileToConfig(String configFile) {
		InputStream fis = null;
		StringBuilder s = new StringBuilder();
		try {
			fis = new FileInputStream(configFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line;

			while ((line = br.readLine()) != null) {
				s.append(line);
			}
			
			br.close();
			fis.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Gson().fromJson(s.toString(), JsonObject.class);
	}

	public static String getLocalConfigValue(String ConfigKey) {
		String configValue = null;
		System.out.println(localConfig.toString());

		configValue = localConfig.getAsJsonObject("configuration").get(ConfigKey)
				.getAsString();

		return configValue;
	}

	public static String getRmConfigValue(String ConfigKey) {
		String configValue = null;
		System.out.println(rmConfig.toString());

		configValue = rmConfig.getAsJsonObject("configuration").get(ConfigKey)
				.getAsString();

		return configValue;
	}
	
	public static String getSeleniumVersion() {
		return getRmConfigValue("seleniumVersion");
	}	
	
	public static String getAndroidHome() {
		return getLocalConfigValue("androidHome");
	}

	public static String getHubIp() {
		return getLocalConfigValue("hubIp");
	}

	public static String getLocalIp() {
		return getLocalConfigValue("localIp");
	}

	public static String getBuildToolsVersion() {
		return getLocalConfigValue("AndroidBuildtoolsVersion");
	}

	public static boolean runOnGrid() {
		Boolean runOnGrid = false;
		try {
			runOnGrid = Boolean.valueOf(getLocalConfigValue("runOnGrid"));
		} catch (Exception e) {
			
		}
		return runOnGrid;
	}
	
	public static boolean usePhantomJS(){
		boolean usePhantomJS = false;
		try {
			usePhantomJS = Boolean.valueOf(getLocalConfigValue("usePhantomJS"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return usePhantomJS;
	}

	public static boolean useChrome() {
		boolean useChrome = false;
		try {
			useChrome = Boolean.valueOf(getLocalConfigValue("useChrome"));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return useChrome;
	}

}