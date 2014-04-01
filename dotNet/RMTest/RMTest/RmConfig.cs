using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json.Linq;
using System.IO;
using Newtonsoft.Json;

namespace RMTest
{
    class RmConfig
    {
        String configFile = TestHome.main() + "\\etc\\LocalConfig.json";

         JObject config;

        public RmConfig()
        {
            StreamReader re = File.OpenText(configFile);
            JsonTextReader reader = new JsonTextReader(re);
            config = JObject.Load(reader);

        }

        public  String getConfigValue(String ConfigKey) {
            String rmconfig = null;


            rmconfig = config.GetValue("configuration").ToString();
            Console.WriteLine(rmconfig);
            JObject configValue = JObject.Parse(rmconfig);

            return configValue.GetValue(ConfigKey).ToString();
        }

        public  String getTestHome()
        {
            return getConfigValue("testHome");
        }

        public  String getAndroidHome()
        {
            return getConfigValue("androidHome");
        }

        public  String getHubIp()
        {
            Console.WriteLine("HubIp: " + getConfigValue("hubIp"));
            return getConfigValue("hubIp");
        }

        public  String getLocalIp()
        {
            return getConfigValue("localIp");
        }
    }
}
