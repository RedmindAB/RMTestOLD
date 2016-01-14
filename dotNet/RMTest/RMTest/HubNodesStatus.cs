using System;
using System.Collections.Generic;
using System.Collections;
using System.IO;
using System.Linq;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json.Converters;
using System.Runtime.Serialization;
using System.Net;
using Newtonsoft.Json.Linq;
using OpenQA.Selenium;
using OpenQA.Selenium.Support;
using OpenQA.Selenium.Remote;



namespace RMTest
{
	public class HubNodesStatus
	{

		private JArray nodesAsJson;
		private List<RegistrationRequest> nodesAsRegReqs = new List<RegistrationRequest>();
		//    private static RegistrationRequest regReq = new RegistrationRequest();

	
/*		public HubNodesStatus(String pHost, int pPort)
		{
			try
			{
				NodeInfoFromHub nodeInfo = new NodeInfoFromHub();
				JObject jO = JObject.Parse(nodeInfo.makeRequest(pHost, pPort));
				nodesAsJson = JArray.Parse(jO.GetValue("FreeProxies").ToString());
				//nodesAsJson = NodeInfoFromHub.main(pHost, pPort).getJSONArray("FreeProxies");
				//RegistrationRequest currentNode = new RegistrationRequest();
				//for(int i = 0; i < nodesAsJson.length(); i++)
				//{

				//	currentNode = RegistrationRequest.getNewInstance(nodesAsJson.getJSONObject(i).toString());
				//	//                System.out.println(currentNode.getConfigAsString("port"));
				//	nodesAsRegReqs.add(currentNode);
				//}
			}
			catch(Exception e)
			{
				// TODO Auto-generated catch block
				//Console.WriteLine(e.Message);
			}
		}
*/
/*		public JArray getNodesAsJson()
		{
			return nodesAsJson;
		}
*/


/*        private List<RegistrationRequest> nodesAsRegReqs = new List<RegistrationRequest>();
        */
        // private static RegistrationRequest regReq = new RegistrationRequest();




        public HubNodesStatus(String pHost, int pPort)
        {
            try
            {
                JToken freeNodes = NodeInfoFromHub.main(pHost, pPort)["FreeProxies"]; // getAsJsonArray("FreeProxies");
                nodesAsJson = (JArray)freeNodes;
                RegistrationRequest currentNode = new RegistrationRequest();
                foreach (JObject node in nodesAsJson) // int i = 0; i < nodesAsJson.Count(); i++)
                {
                    //System.Console.WriteLine(nodesAsJson.get(i).getAsJsonObject().toString());

                    currentNode = getRegRequest(node); //   JObject.FromObject(node));
                    // System.out.println(currentNode.getConfigAsString("port"));
                    nodesAsRegReqs.Add(currentNode);
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                Console.WriteLine(e.Message);
            }
        }

    public static RegistrationRequest getRegRequest(JObject node)
        {
            RegistrationRequest request = new RegistrationRequest();

            JObject configuration = (JObject)node["configuration"];
            Dictionary<String, Object> config = new Dictionary<string, object>();
		// For backward compatibility numbers should be converted to integers
		foreach (var keyValuePair in configuration)
        {
                String key = keyValuePair.Key;
                Object value = keyValuePair.Value;
            
			if (value is long)
            {
                //config.Remove(keyValuePair.Key);
				config.Add(key, ((int)value).ToString()); //(key, ((Long) value).intValue());
			}
            else
            {
                    config.Add(key, value.ToString());
            }
        }
        request.setConfiguration(config);

		JArray capabilities = (JArray)node["capabilities"];
        Dictionary<String, Object> caps;
        //DesiredCapabilities dc = new DesiredCapabilities();

        foreach (JObject capability in capabilities)// int i = 0; i<capabilities.Count; i++) 
        {
                
                caps = new Dictionary<string, object>();
                foreach (var keyValuePair in capability)
                {
                    //dc.SetCapability(keyValuePair.Key, keyValuePair.Value.ToString());

                    caps.Add(keyValuePair.Key, keyValuePair.Value.ToString());
                    //DesiredCapabilities cap = new DesiredCapabilities(caps);//new JsonToBeanConverter().DesiredCapabilities.class, capabilities.get(i));
                }
                //request.addDesiredCapability(dc);
                request.addDesiredCapability(caps);
        }
		return request;

	}
	// public JsonArray getNodesAsJson(){
	// return nodesAsJson;
	// }

	public List<RegistrationRequest> GetNodesAsRegReqs()
{
    return nodesAsRegReqs;
}





	}
}
