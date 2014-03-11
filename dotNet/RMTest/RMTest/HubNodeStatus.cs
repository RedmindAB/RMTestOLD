using System;
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
		//private ArrayList<RegistrationRequest> nodesAsRegReqs = new ArrayList<RegistrationRequest>();
		//    private static RegistrationRequest regReq = new RegistrationRequest();

	
		public HubNodesStatus(String pHost, int pPort)
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
				Console.WriteLine(e.Message);
			}
		}

		public JArray getNodesAsJson()
		{
			return nodesAsJson;
		}

	



	}
}
