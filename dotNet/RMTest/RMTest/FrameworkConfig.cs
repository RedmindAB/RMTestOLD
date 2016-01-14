using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json.Linq;

namespace RMTest
{
    class FrameworkConfig
    {


		private JObject localConfig; // Add "final" (JAVA) equivalent

        public static FrameworkConfig getConfig()
        {
            return new FrameworkConfig();
        }

        public FrameworkConfig(String localConfigFile)
		{
			this.localConfig = parse(localConfigFile);
		}

		private FrameworkConfig()
		{
            this.localConfig = parse(TestHome.main() + "/Grid/etc/LocalConfig.json");
		}

		private static JObject parse(String configFile)
		{
			try
			{
                String json = File.ReadAllText(configFile); //IOUtils.toString(configFile);
                return Newtonsoft.Json.Linq.JObject.Parse(json); //Gson().fromJson(json, JObject.class);
		    }
            catch (IOException e)
            {
                throw e; // new System.RuntimeException(e);
            }
	}

	    private String getLocalConfigValue(String configKey)
        {
            //return this.localConfig.getAsJsonObject("configuration").get(configKey).getAsString();
            return this.localConfig.SelectToken("configuration." + configKey).ToString();
        }

        public bool runOnGrid()
        {
            return "true".Equals(this.getLocalConfigValue("runOnGrid").ToLower());
            //return "true".equalsIgnoreCase(this.getLocalConfigValue("runOnGrid"));
        }

        public bool usePhantomJS()
        {
	        return "true".Equals(this.getLocalConfigValue("usePhantomJS").ToLower());
        }

        public bool useFirefox()
        {
	        return "true".Equals(this.getLocalConfigValue("useFirefox").ToLower());
        }

        public bool useChrome()
        {
	        return "true".Equals(this.getLocalConfigValue("useChrome").ToLower());
        }

        public bool autoCloseDrivers()
        {
	        return "true".Equals(getLocalConfigValue("autoCloseDrivers").ToLower());
        }

        public String getRMRLiveAddress()
        {
	        //return Objects.firstNonNull(getLocalConfigValue("RmReportIP"), "127.0.0.1");
            return getLocalConfigValue("RmReportIP") ?? "127.0.0.1";
        }

        public int getRMRLivePort()
        {
	        return int.Parse(getLocalConfigValue("RmReportLivePort") ?? "12345");
        }

        public bool enableLiveStream()
        {
	        return "true".Equals(getLocalConfigValue("enableLiveStream"));
        }

        public String getJsonReportSavePath()
        {
	        return getLocalConfigValue("jsonReportSavePath") ?? TestHome.main() + "/target/RMTReports";
        }

        public String getAndroidHome()
        {
	        return this.getLocalConfigValue("androidHome");
        }

        public String getHubIp()
        {
	        return this.getLocalConfigValue("hubIp");
        }

        public String getLocalIp()
        {
	        return this.getLocalConfigValue("localIp");
        }

        public String getBuildToolsVersion()
        {
	        return this.getLocalConfigValue("AndroidBuildtoolsVersion");
        }
    }
}
